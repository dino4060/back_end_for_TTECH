package com.dino.back_end_for_TTECH.shared.application.utils;

import java.util.function.Consumer;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppMapper {
  public <T> void patch(T value, Consumer<T> setFunc) {
    if (value != null) {
      setFunc.accept(value);
    }
  }
}
