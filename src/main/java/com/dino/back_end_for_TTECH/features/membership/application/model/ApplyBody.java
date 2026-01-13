package com.dino.back_end_for_TTECH.features.membership.application.model;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplyBody {

  @NotNull(message = "body.spendAmount is required")
  Integer spendAmount;
}