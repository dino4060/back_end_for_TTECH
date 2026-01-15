package com.dino.back_end_for_TTECH.features.membership.application.model;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MemberData {
  Integer points;

  MembershipData membership;

  LocalDateTime rankedAt;

  String status;

  @Data
  @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
  public static class MembershipData {
    String membershipCode;
  }
}
