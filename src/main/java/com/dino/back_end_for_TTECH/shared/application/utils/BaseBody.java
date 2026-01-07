package com.dino.back_end_for_TTECH.shared.application.utils;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseBody {
  Long id;
}