package com.dino.back_end_for_TTECH.features.promotion.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Type;

import com.dino.back_end_for_TTECH.features.profile.domain.User;
import com.dino.back_end_for_TTECH.features.promotion.domain.model.CouponApplyResult;
import com.dino.back_end_for_TTECH.features.promotion.domain.model.PromoType;
import com.dino.back_end_for_TTECH.features.promotion.domain.model.Status;
import com.dino.back_end_for_TTECH.shared.application.utils.AppGen;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "vouchers")
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "UPDATE vouchers SET is_deleted_child = true WHERE campaign_id=?")
@SQLRestriction("is_deleted_child = false")
@DiscriminatorValue("VOUCHER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "promotion_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class Coupon extends Campaign {

  String couponType;

  String couponCode;

  Boolean isFixed;

  Integer discountValue;

  Integer minSpend;

  Integer maxDiscount;

  Integer validityDays;

  Integer totalLimit;

  Integer limitPerCustomer;

  Integer usedCount = 0;

  @Type(JsonType.class)
  @Column(columnDefinition = "jsonb")
  Map<Long, Integer> countPerCustomer = new HashMap<>();

  Boolean isApplyAll;

  @OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  List<CouponUnit> units = new ArrayList<>();

  Boolean isDeletedChild = false;

  @OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY)
  List<CustomerCoupon> customerCoupons = new ArrayList<>();

  public Coupon(long id) {
    this.id = id;
  }

  /**
   * Check if coupon can be applied to the order
   * 
   * @param customerId  Customer ID applying the coupon
   * @param spendAmount Total order amount before discount
   * @param productIDs  List of products in the order
   * @return CouponApplyResult with application status, discount amount, and
   *         message
   */
  public CouponApplyResult canApply(Long customerId, Integer spendAmount, List<Long> productIDs) {
    // 2. Check coupon status (must be ONGOING)
    if (!hasStatus(Status.ONGOING)) {
      return CouponApplyResult.fail("Coupon không hoạt động");
    }

    // 3. Check time validity (extra safety check)
    LocalDateTime now = LocalDateTime.now();
    if (this.startTime != null && now.isBefore(this.startTime)) {
      return CouponApplyResult.fail("Coupon is not yet valid");
    }
    if (this.endTime != null && now.isAfter(this.endTime)) {
      return CouponApplyResult.fail("Coupon has expired");
    }

    // 4. Check total usage limit
    if (this.totalLimit != null && this.usedCount >= this.totalLimit) {
      return CouponApplyResult.fail("Coupon đã hết lượt sử dụng");
    }

    // 5. Check customer usage limit
    if (customerId != null && this.limitPerCustomer != null) {
      Integer customerUsage = this.countPerCustomer.getOrDefault(customerId, 0);
      if (customerUsage >= this.limitPerCustomer) {
        return CouponApplyResult.fail("Bạn đã hết lượt sử dụng coupon");
      }
    }

    // 6. Check minimum spend requirement
    if (this.minSpend != null && spendAmount < this.minSpend) {
      return CouponApplyResult.fail(
          String.format("Chi tiêu tối thiểu %,d,000 VND để sử dụng coupon (hiện tại: %,d,000 VND)",
              this.minSpend, spendAmount));
    }

    // 7. Check product eligibility (if not apply to all products)
    if (Boolean.FALSE.equals(this.isApplyAll)) {
      if (!isProductListEligible(productIDs)) {
        return CouponApplyResult.fail("Coupon không sử dụng cho sản phẩm này");
      }
    }

    // 8. Calculate discount amount
    Integer discountAmount = calculateDiscount(spendAmount);

    // 9. Success
    return CouponApplyResult.success(this.getPromotionType(), this.getId(), this.getCouponCode(), discountAmount);
  }

  /**
   * Check if at least one product in the list is eligible for the coupon
   */
  private boolean isProductListEligible(List<Long> productIDs) {
    if (productIDs == null || productIDs.isEmpty()) {
      return false;
    }

    // Get eligible product IDs from coupon units
    List<Long> eligibleProductIDs = this.units.stream()
        .map(unit -> unit.getProduct().getId())
        .toList();

    // Check if any product in the order is eligible
    return productIDs.stream()
        .anyMatch(productID -> eligibleProductIDs.contains(productID));
  }

  /**
   * Check if customer has remaining usage quota for coupon
   * 
   * @param coupon     Coupon to check
   * @param customerId Customer ID
   * @return true if customer can still use this coupon
   */
  public boolean hasCustomerQuota(Long customerId) {
    if (this.getLimitPerCustomer() == null)
      return true;

    Integer customerUsage = this.getCountPerCustomer().getOrDefault(customerId, 0);

    return customerUsage < this.getLimitPerCustomer();
  }

  /**
   * Calculate discount amount based on coupon type (fixed or percentage)
   */
  private Integer calculateDiscount(Integer spendAmount) {
    Integer discount;

    if (Boolean.TRUE.equals(this.isFixed)) {
      // Fixed discount
      discount = this.discountValue;
    } else {
      // Percentage discount
      discount = (spendAmount * this.discountValue) / 100;

      // Apply max discount cap if exists
      if (this.maxDiscount != null && discount > this.maxDiscount) {
        discount = this.maxDiscount;
      }
    }

    // Discount cannot exceed spend amount
    if (discount > spendAmount) {
      discount = spendAmount;
    }

    return discount;
  }

  public CustomerCoupon claimBy(User customer) {
    var claim = new CustomerCoupon();
    claim.setCustomer(customer);
    claim.setCoupon(this);
    claim.setClaimedAt(LocalDateTime.now());

    if (this.validityDays != null) {
      claim.setExpiresAt(LocalDateTime.now().plusDays(this.validityDays));
    }

    return claim;
  }

  /**
   * Mark coupon as used by customer
   * Should be called after successful order placement
   */
  public void markAsUsed(Long customerId) {
    // Increment total usage
    this.usedCount++;

    // Increment customer usage
    if (customerId != null) {
      Integer currentUsage = this.countPerCustomer.getOrDefault(customerId, 0);
      this.countPerCustomer.put(customerId, currentUsage + 1);
    }
  }

  /**
   * Rollback coupon usage (for order cancellation)
   */
  public void rollbackUsage(Long customerId) {
    // Decrement total usage
    if (this.usedCount > 0) {
      this.usedCount--;
    }

    // Decrement customer usage
    if (customerId != null) {
      Integer currentUsage = this.countPerCustomer.getOrDefault(customerId, 0);
      if (currentUsage > 0) {
        this.countPerCustomer.put(customerId, currentUsage - 1);
      }
    }
  }

  /**
   * Description
   * - Sync status based on dates
   * - Generate coupon code if needed
   * - Clear max discount if fixed discount
   */
  public void processIntegrity() {
    this.refreshStatus();
    this.processCouponType();
    this.processCouponCode();
    this.processMaxDiscount();
  }

  /**
   * Description:
   * - Set coupon type same promotion type
   */
  private void processCouponType() {
    this.setCouponType(this.getPromotionType());
  }

  /**
   * Description:
   * - Clear code if not CODE_VOUCHER type
   * - Generate random code if CODE_VOUCHER and code is null
   */
  private void processCouponCode() {
    var isCouponCode = PromoType.COUPON_CODE.toString().equals(this.getPromotionType());

    if (!isCouponCode) {
      this.setCouponCode(null);
    } else if (this.getCouponCode() == null) {
      this.setCouponCode(AppGen.randomCode(8));
    }
  }

  /**
   * Description:
   * - Clear max discount if using fixed discount
   */
  private void processMaxDiscount() {
    var isFixed = Boolean.TRUE.equals(this.getIsFixed());
    var isCouponCode = PromoType.COUPON_CODE.toString().equals(this.getPromotionType());

    if (isFixed) {
      this.setMaxDiscount(null);
    } else if (isCouponCode && this.getMaxDiscount() != null) {
      this.setMaxDiscount(1);
    }
  }
}