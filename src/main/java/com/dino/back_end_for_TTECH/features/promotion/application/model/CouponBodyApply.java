package com.dino.back_end_for_TTECH.features.promotion.application.model;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class CouponBodyApply {
  Long id;
  String couponCode;
  Integer spendAmount;
  List<Long> productIDs;
}
