package com.dino.back_end_for_TTECH.features.membership.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyResult {

  Boolean isApplied;
  Long id;
  String membershipCode;
  String benefitType;
  String benefitName;
  Integer benefitValue;
  String message;

  public static ApplyResult success(Long id, String benefitType, String benefitName, Integer benefitValue) {
    return new ApplyResult(true, id, null, benefitType, benefitName, benefitValue, "Benefit applied successfully");
  }

  public static ApplyResult fail(String message) {
    return new ApplyResult(false, null, null, null, null, 0, message);
  }
}
