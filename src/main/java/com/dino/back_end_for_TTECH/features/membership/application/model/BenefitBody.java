package com.dino.back_end_for_TTECH.features.membership.application.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BenefitBody {

  Long id;

  @NotBlank(message = "Benefit type is required")
  String benefitType;

  @NotBlank(message = "Benefit name is required")
  String benefitName;

  @Min(value = 0, message = "Benefit value must be >= 0")
  Integer benefitValue;

  String benefitUnit;

  Integer minSpend;

  Integer limitPerCustomer;

  Boolean isAlive;
}