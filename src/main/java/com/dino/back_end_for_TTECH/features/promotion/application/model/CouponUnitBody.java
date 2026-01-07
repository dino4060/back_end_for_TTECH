package com.dino.back_end_for_TTECH.features.promotion.application.model;

import com.dino.back_end_for_TTECH.shared.application.utils.ObjectId;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CouponUnitBody {

  Long id;

  @Valid
  @NotNull(message = "CouponUnitBody.product is required")
  ObjectId product;
}
