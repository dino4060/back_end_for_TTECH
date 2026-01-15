package com.dino.back_end_for_TTECH.features.membership.application.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MembershipBody {

  Long id;

  String membershipName;

  @NotBlank(message = "Membership code is required")
  String membershipCode;

  @Min(value = 0, message = "Min point must be >= 0")
  Integer minPoint;

  List<BenefitBody> benefits = new ArrayList<>();

  Boolean isAlive;
}