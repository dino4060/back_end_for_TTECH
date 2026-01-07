package com.dino.back_end_for_TTECH.shared.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InternalServerE extends ResponseStatusException {
  public InternalServerE(String message) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }
}
