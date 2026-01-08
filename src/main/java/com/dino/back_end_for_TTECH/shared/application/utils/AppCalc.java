package com.dino.back_end_for_TTECH.shared.application.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppCalc {
  public int restOfDiscountPercent(int discountPercent, int total) {
    double discountAmount = total * (discountPercent / 100.0);

    return (int) (total - discountAmount);
  }

  public int discountPercentOfRest(int part, int total) {
    if (total <= 0)
      return 0;

    int discountAmount = total - part;
    return (discountAmount * 100) / total;

  }
}
