package com.dino.back_end_for_TTECH.features.promotion.application.model;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponBodyApply {
  Long id;
  String couponCode;

  @NotNull(message = "coupon.spendAmount is required")
  Integer spendAmount;

  @NotEmpty(message = "coupon.productIDs is not empty")
  List<Long> productIDs;
}
