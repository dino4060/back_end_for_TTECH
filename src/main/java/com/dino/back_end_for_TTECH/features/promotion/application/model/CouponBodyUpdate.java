package com.dino.back_end_for_TTECH.features.promotion.application.model;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponBodyUpdate extends CouponBody {

  @NotNull(message = "CouponBodyUpdate.id is required")
  Long id;
}
