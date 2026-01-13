package com.dino.back_end_for_TTECH.features.promotion.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponApplyResult {

  Boolean isApplied;
  String promotionType;
  Long id;
  String couponCode;
  Integer discountAmount;
  String message;

  // Factory methods for common cases
  public static CouponApplyResult success(String promoType, Long id, String couponCode, Integer discountAmount) {
    return new CouponApplyResult(true, promoType, id, couponCode, discountAmount, "Coupon applied successfully");
  }

  public static CouponApplyResult success(String message, String promoType, Long id, Integer discountAmount) {
    return new CouponApplyResult(true, promoType, id, null, discountAmount, message);
  }

  public static CouponApplyResult fail(String message) {
    return new CouponApplyResult(false, null, null, null, 0, message);
  }
}
