package com.dino.back_end_for_TTECH.features.membership.application.model;

import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BenefitData {
  Long id;
  String benefitType;
  String benefitName;
  Integer benefitValue;
  String benefitUnit;
  Integer minSpend;
  Integer validityMonths;
  Integer limitPerCustomer;
  Map<Long, Integer> countPerCustomer;
}
