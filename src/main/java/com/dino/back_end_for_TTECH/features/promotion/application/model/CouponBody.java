package com.dino.back_end_for_TTECH.features.promotion.application.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponBody extends CampaignBody {

  @Pattern(regexp = "^[A-Z0-9]+$", message = "CouponBody.couponCode should be uppercase and numeric")
  @Size(min = 2, max = 20, message = "CouponBody.couponCode should have 2-20 characters")
  String couponCode;

  @NotNull(message = "CouponBody.isFixed is required")
  Boolean isFixed;

  @NotNull(message = "CouponBody.discountValue is required")
  Integer discountValue;

  Integer minSpend;

  Integer maxDiscount;

  Integer validityDays;

  Integer totalLimit;

  Integer limitPerCustomer;

  @NotNull(message = "CouponBody.isApplyAll is required")
  Boolean isApplyAll;

  @Valid
  List<CouponUnitBody> units = new ArrayList<>();
}
