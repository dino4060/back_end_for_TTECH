package com.dino.back_end_for_TTECH.features.membership.application.model;

import java.time.Instant;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MembershipData {
  Long id;
  String membershipName;
  String membershipCode;
  Integer minPoint;
  List<BenefitData> benefits;
  Boolean isAlive;
  Instant createdAt;
  Instant updatedAt;
  Integer points;
}
