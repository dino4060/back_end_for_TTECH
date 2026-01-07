package com.dino.back_end_for_TTECH.features.promotion.application.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponBody extends CampaignBody {

  Boolean isFixed;

  Integer discountValue;

  Integer minSpend;

  Integer maxDiscount;

  Integer validityDays;

  Integer totalLimit;

  Integer limitPerCustomer;

  Boolean isApplyAll;

  @Valid
  List<CouponUnitBody> units = new ArrayList<>();
}
