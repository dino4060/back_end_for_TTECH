package com.dino.back_end_for_TTECH.shared.application.utils;

import com.dino.back_end_for_TTECH.shared.application.exception.BadRequestE;

public class AppValidation {
  public static void notNull(Long number, String message) {
    if (number == null) {
      throw new BadRequestE(message);
    }
  }
}
