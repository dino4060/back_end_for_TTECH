package com.dino.back_end_for_TTECH.features.promotion.domain.specification;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

import com.dino.back_end_for_TTECH.features.promotion.domain.Coupon;
import com.dino.back_end_for_TTECH.features.promotion.domain.model.PromoType;
import com.dino.back_end_for_TTECH.features.promotion.domain.model.Status;

import jakarta.persistence.criteria.JoinType;

public class CouponSpec {

  /**
   * Coupon applies to all products OR specific product
   */
  public static Specification<Coupon> forProduct(Long productId) {
    return (root, query, builder) -> {
      if (productId == null) {
        return null;
      }

      // isApplyAll = true OR product exists in units
      var applyAll = builder.isTrue(root.get("isApplyAll"));

      var unitsJoin = root.join("units", JoinType.LEFT);
      var hasProduct = builder.equal(unitsJoin.get("product").get("id"), productId);

      return builder.or(applyAll, hasProduct);
    };
  }

  /**
   * Coupon type is ORDER_COUPON or SHIPPING_COUPON
   */
  public static Specification<Coupon> isClaimedCouponType() {
    return (root, query, builder) -> {
      Set<String> claimedCouponTypes = Set.of(
          PromoType.ORDER_COUPON.toString(),
          PromoType.SHIPPING_COUPON.toString());

      return root.get("couponType").in(claimedCouponTypes);
    };
  }

  public static Specification<Coupon> hasStatusIn(Set<Status> statusList) {
    return (root, query, builder) -> {
      if (statusList == null || statusList.isEmpty())
        return null;

      Set<String> statusTexts = statusList.stream()
          .map(Status::name)
          .collect(Collectors.toSet());

      return root.get("status").in(statusTexts);
    };
  }

  /**
   * Coupon has available slots (totalLimit > usedCount)
   */
  public static Specification<Coupon> hasAvailableSlots() {
    return (root, query, builder) -> {
      // totalLimit is null (unlimited) OR totalLimit > usedCount
      var noLimit = builder.isNull(root.get("totalLimit"));

      var hasSlots = builder.greaterThan(
          root.get("totalLimit"),
          root.get("usedCount"));

      return builder.or(noLimit, hasSlots);
    };
  }
}