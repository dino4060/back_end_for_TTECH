package com.dino.back_end_for_TTECH.shared.application.utils;

import java.util.Random;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

@UtilityClass
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppGen {
  String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  Random RANDOM = new Random();

  public String randomCode(int length) {
    StringBuilder code = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int randomIndex = RANDOM.nextInt(CHARS.length());
      code.append(CHARS.charAt(randomIndex));
    }
    return code.toString();
  }
}
