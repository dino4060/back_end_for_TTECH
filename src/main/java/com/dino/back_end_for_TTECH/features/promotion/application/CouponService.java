package com.dino.back_end_for_TTECH.features.promotion.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.dino.back_end_for_TTECH.features.product.domain.repository.ProductRepository;
import com.dino.back_end_for_TTECH.features.promotion.application.mapper.CouponMapper;
import com.dino.back_end_for_TTECH.features.promotion.application.model.CouponBody;
import com.dino.back_end_for_TTECH.features.promotion.application.model.CouponBodyApply;
import com.dino.back_end_for_TTECH.features.promotion.application.model.CouponBodyPatch;
import com.dino.back_end_for_TTECH.features.promotion.application.model.CouponBodyUpdate;
import com.dino.back_end_for_TTECH.features.promotion.application.model.CouponData;
import com.dino.back_end_for_TTECH.features.promotion.application.model.CouponDataSaved;
import com.dino.back_end_for_TTECH.features.promotion.application.model.CouponUnitBody;
import com.dino.back_end_for_TTECH.features.promotion.domain.Coupon;
import com.dino.back_end_for_TTECH.features.promotion.domain.CouponUnit;
import com.dino.back_end_for_TTECH.features.promotion.domain.model.CouponApplyResult;
import com.dino.back_end_for_TTECH.features.promotion.domain.repository.CouponRepository;
import com.dino.back_end_for_TTECH.shared.api.annotation.AuthUser;
import com.dino.back_end_for_TTECH.shared.api.model.CurrentUser;
import com.dino.back_end_for_TTECH.shared.application.exception.BadRequestE;
import com.dino.back_end_for_TTECH.shared.application.utils.AppMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CouponService {

  CouponRepository couponRepo;
  CouponMapper couponMapper;

  CampaignHelper campaignService;

  ProductRepository productRepo;

  @Transactional
  public CouponApplyResult applyCoupon(
      @AuthUser CurrentUser customer,
      @RequestBody CouponBodyApply body) {

    // Find coupon by code
    Coupon coupon = couponRepo
        .findByCouponCode(body.getCouponCode())
        .orElseThrow(() -> new BadRequestE("Invalid coupon code"));

    // Check if coupon can be applied
    CouponApplyResult result = coupon.canApply(customer.id(), body.getSpendAmount(), body.getProductIDs());

    if (!result.getIsApplied()) {
      return result;
    }

    // Mark as used (will be saved when transaction commits)
    coupon.markAsUsed(customer.id());

    return result;
  }

  @Transactional
  public void applyCoupon(
      @AuthUser CurrentUser customer,
      @RequestBody CouponApplyResult appliedCoupon) {

    if (!appliedCoupon.getIsApplied()) {
      return;
    }

    // Find coupon by code
    Coupon coupon = couponRepo
        .findById(appliedCoupon.getId())
        .orElseThrow(() -> new BadRequestE("Coupon not found"));

    // Mark as used (will be saved when transaction commits)
    coupon.markAsUsed(customer.id());
  }

  @Transactional(readOnly = true)
  public CouponApplyResult preview(
      @AuthUser CurrentUser customer,
      @RequestBody CouponBodyApply body) {

    if (body.getId() != null) {
      return couponRepo.findById(body.getId())
          .map(coupon -> coupon.canApply(customer.id(), body.getSpendAmount(), body.getProductIDs()))
          .orElseGet(() -> CouponApplyResult.fail("Không tìm thấy Coupon"));
    }

    if (body.getCouponCode() != null) {
      return couponRepo.findByCouponCode(body.getCouponCode())
          .map(coupon -> coupon.canApply(customer.id(), body.getSpendAmount(), body.getProductIDs()))
          .orElseGet(() -> CouponApplyResult.fail("Không tìm thấy mã Coupon: " + body.getCouponCode()));
    }

    throw new BadRequestE("CouponBodyApply.id or .couponCode is required");
  }

  public CouponData get(long id) {
    var coupon = this.couponRepo.getById(id);

    this.campaignService.saveSyncStatus(coupon);

    return this.couponMapper.toData(coupon);
  }

  @Transactional
  public void delete(Coupon coupon) {
    this.couponRepo.delete(coupon);
  }

  @Transactional
  public CouponDataSaved patch(CouponBodyPatch body) {
    var coupon = this.couponRepo.getById(body.getId());

    AppMapper.patch(body.getStatus(), coupon::setStatus);

    var savedCoupon = this.couponRepo.save(coupon);

    return this.couponMapper.toDataSaved(savedCoupon);
  }

  @Transactional
  public CouponDataSaved update(CouponBodyUpdate body) {
    var coupon = this.couponRepo.getById(body.getId());

    // Convert body to coupon (exclude units)
    this.couponMapper.toModel(body, coupon);

    // Process fields
    coupon.processIntegrity();

    // Process coupon units (products)
    if (coupon.getIsApplyAll() == false) {
      this.processCouponUnits(coupon, body.getUnits());
    }

    // Save coupon (cascade and orphanRemoval will save units)
    var savedCoupon = this.couponRepo.save(coupon);

    return this.couponMapper.toDataSaved(savedCoupon);
  }

  @Transactional
  public CouponDataSaved create(CouponBody body) {
    // Convert body to coupon (exclude units)
    var coupon = this.couponMapper.toModel(body);

    // Process fields
    coupon.processIntegrity();

    // Process coupon units (products)
    if (coupon.getIsApplyAll() == false) {
      List<CouponUnit> newUnits = body.getUnits().stream()
          .map(unitBody -> this.createCouponUnit(coupon, unitBody))
          .collect(Collectors.toList());

      coupon.getUnits().addAll(newUnits);
    }

    // Save coupon (cascade will save units)
    var savedCoupon = this.couponRepo.save(coupon);

    return this.couponMapper.toDataSaved(savedCoupon);
  }

  /**
   * Process CouponUnit list by "Compare 3 status" strategy
   * - Unit doesn't have id => CREATE
   * - Unit has id and exists in DB => UPDATE
   * - Unit exists in DB but doesn't exist in body => DELETE
   */
  private void processCouponUnits(Coupon coupon, List<CouponUnitBody> unitBodyList) {
    Map<Long, CouponUnit> currUnits = coupon.getUnits().stream()
        .filter(unit -> unit.getId() != null)
        .collect(Collectors.toMap(unit -> unit.getId(), unit -> unit));

    List<CouponUnit> updatedUnits = unitBodyList.stream()
        .map(unitBody -> {
          if (unitBody.getId() != null && currUnits.containsKey(unitBody.getId())) {
            // UPDATE
            CouponUnit existingUnit = currUnits.remove(unitBody.getId());
            this.updateCouponUnit(existingUnit, unitBody);
            return existingUnit;
          } else {
            // CREATE
            return this.createCouponUnit(coupon, unitBody);
          }
        })
        .toList();

    // DELETE
    coupon.getUnits().clear();
    coupon.getUnits().addAll(updatedUnits);
  }

  private void updateCouponUnit(CouponUnit unit, CouponUnitBody body) {
    Long newProductId = body.getProduct().getId();
    Long currProductId = unit.getProduct() != null ? unit.getProduct().getId() : null;

    // Update product field
    if (!newProductId.equals(currProductId)) {
      var product = this.productRepo.getIdById(newProductId);
      unit.setProduct(product);
    }
  }

  private CouponUnit createCouponUnit(Coupon coupon, CouponUnitBody body) {
    var product = this.productRepo.getIdById(body.getProduct().getId());

    var unit = new CouponUnit();
    unit.setCoupon(coupon);
    unit.setProduct(product);

    return unit;
  }
}
