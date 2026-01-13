package com.dino.back_end_for_TTECH.features.membership.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dino.back_end_for_TTECH.features.membership.application.MembshipService;
import com.dino.back_end_for_TTECH.shared.api.annotation.AuthUser;
import com.dino.back_end_for_TTECH.shared.api.model.CurrentUser;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/memberships")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MembershipController {

  MembshipService membshipService;

  @GetMapping
  public ResponseEntity<?> getByCustomer(@AuthUser CurrentUser customer) {
    var data = membshipService.getByCustomer(customer.id());
    return ResponseEntity.ok(data);
  }
}