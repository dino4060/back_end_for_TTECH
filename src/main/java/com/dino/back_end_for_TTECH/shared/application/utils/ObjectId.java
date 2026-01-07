package com.dino.back_end_for_TTECH.shared.application.utils;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ObjectId {
  @NotNull(message = "ObjectId.id is required")
  Long id;
}