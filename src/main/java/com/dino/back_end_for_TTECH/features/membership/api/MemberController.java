package com.dino.back_end_for_TTECH.features.membership.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dino.back_end_for_TTECH.features.membership.application.MemberService;
import com.dino.back_end_for_TTECH.shared.api.annotation.AuthUser;
import com.dino.back_end_for_TTECH.shared.api.model.CurrentUser;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MemberController {

  MemberService memberService;

  @GetMapping
  public ResponseEntity<?> offer(@AuthUser CurrentUser customer) {
    var data = memberService.offerMembership(customer.id());
    return ResponseEntity.ok(data);
  }
}