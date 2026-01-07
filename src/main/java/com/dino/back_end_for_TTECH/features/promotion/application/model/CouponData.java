package com.dino.back_end_for_TTECH.features.promotion.application.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class CouponData extends CampaignData {

  Boolean isFixed;

  Integer discountValue;

  Integer minSpend;

  Integer maxDiscount;

  Integer validityDays;

  Integer totalLimit;

  Integer limitPerCustomer;

  Boolean isApplyAll;

  List<CouponUnitData> units;
}
