package com.dino.back_end_for_TTECH.features.promotion.application.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponBodyPatch {
  @NotNull(message = "CouponBodyPatch.id is required")
  Long id;

  @Pattern(regexp = "DEACTIVATED", message = "CouponBodyPatch.status should be DEACTIVATED only")
  String status;
}
