package com.dino.back_end_for_TTECH.features.promotion.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.dino.back_end_for_TTECH.features.product.domain.repository.ProductRepository;
import com.dino.back_end_for_TTECH.features.profile.domain.repository.UserRepository;
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
import com.dino.back_end_for_TTECH.features.promotion.domain.CustomerCoupon;
import com.dino.back_end_for_TTECH.features.promotion.domain.model.CouponApplyResult;
import com.dino.back_end_for_TTECH.features.promotion.domain.model.Status;
import com.dino.back_end_for_TTECH.features.promotion.domain.repository.CouponRepository;
import com.dino.back_end_for_TTECH.features.promotion.domain.repository.CustomerCouponRepository;
import com.dino.back_end_for_TTECH.features.promotion.domain.specification.CouponSpec;
import com.dino.back_end_for_TTECH.shared.api.annotation.AuthUser;
import com.dino.back_end_for_TTECH.shared.api.model.CurrentUser;
import com.dino.back_end_for_TTECH.shared.application.exception.BadRequestE;
import com.dino.back_end_for_TTECH.shared.application.exception.NotFoundE;
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

  UserRepository userRepo;

  CustomerCouponRepository customerCouponRepo;

  @Transactional
  public void claim(long customerId, long couponId) {
    Coupon coupon = couponRepo
        .findById(couponId)
        .orElseThrow(() -> new NotFoundE("Coupon not found"));

    var customer = userRepo
        .findById(customerId)
        .orElseThrow(() -> new NotFoundE("Customer not found"));

    if (customerCouponRepo.existsByCustomerAndCoupon(customer, coupon))
      return;

    var customerCoupon = new CustomerCoupon();
    customerCoupon.setCustomer(customer);
    customerCoupon.setCoupon(coupon);
    customerCoupon.setClaimedAt(LocalDateTime.now());
    customerCoupon.setExpiresAt(coupon.getValidityDays() == null
        ? null
        : LocalDateTime.now().plusDays(coupon.getValidityDays()));

    customerCouponRepo.save(customerCoupon);
  }

  @Transactional
  public void unclaim(long customerId, long couponId) {
    Coupon coupon = couponRepo.findById(couponId).orElseThrow(() -> new NotFoundE("Coupon not found"));

    var customer = userRepo.findById(customerId).orElseThrow(() -> new NotFoundE("Customer not found"));

    customerCouponRepo
        .findByCustomerAndCoupon(customer, coupon)
        .ifPresent(c -> customerCouponRepo.delete(c));
  }

  @Transactional
  public void apply(
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
      @AuthUser long customerId,
      @RequestBody CouponBodyApply body) {

    if (body.getId() != null) {
      return couponRepo.findById(body.getId())
          .map(coupon -> coupon.canApply(customerId, body.getSpendAmount(), body.getProductIDs()))
          .orElseGet(() -> CouponApplyResult.fail("Không tìm thấy Coupon"));
    }

    if (body.getCouponCode() != null) {
      return couponRepo.findByCouponCode(body.getCouponCode())
          .map(coupon -> coupon.canApply(customerId, body.getSpendAmount(), body.getProductIDs()))
          .orElseGet(() -> CouponApplyResult.fail("Không tìm thấy mã Coupon: " + body.getCouponCode()));
    }

    throw new BadRequestE("CouponBodyApply.id or .couponCode is required");
  }

  /**
   * List applicable coupons for customer and product
   * 
   * @param customerId Current authenticated customer
   * @param productId  Product ID to check coupon eligibility
   * @return List of applicable coupons (only ONGOING status)
   */
  @Transactional
  public List<CouponData> list(long customerId, Long productId) {
    List<Coupon> coupons = couponRepo.findAll(Specification
        .where(CouponSpec.forProduct(productId))
        .and(CouponSpec.isClaimedCouponType())
        .and(CouponSpec.hasStatusIn(Set.of(Status.UPCOMING, Status.ONGOING)))
        .and(CouponSpec.hasAvailableSlots()));

    return coupons.stream()
        .peek(coupon -> this.refreshStatusAsync(coupon))
        .filter(coupon -> coupon.hasStatus(Status.ONGOING))
        .filter(coupon -> coupon.hasCustomerQuota(customerId))
        .sorted((coupon, second) -> coupon.getDiscountValue().compareTo(second.getDiscountValue()))
        .map(coupon -> {
          var data = couponMapper.toData(coupon);
          var customer = userRepo.findById(customerId).orElseThrow(() -> new NotFoundE("Customer not found"));
          var isClaimed = this.customerCouponRepo.existsByCustomerAndCoupon(customer, coupon);
          data.setIsClaimed(isClaimed);
          return data;
        })
        .collect(Collectors.toList());
  }

  /**
   * Async update coupon status
   * Coupons with UPCOMING status will be checked and updated if time has come
   */
  @Async
  protected void refreshStatusAsync(Coupon coupon) {
    boolean statusChanged = coupon.refreshStatus();
    if (statusChanged) {
      couponRepo.save(coupon);
      log.debug("Updated coupon {} status to {}", coupon.getId(), coupon.getStatus());
    }
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
