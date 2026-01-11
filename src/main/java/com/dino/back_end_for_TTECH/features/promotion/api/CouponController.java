package com.dino.back_end_for_TTECH.features.promotion.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dino.back_end_for_TTECH.features.promotion.application.CouponService;
import com.dino.back_end_for_TTECH.features.promotion.application.model.CouponBodyApply;
import com.dino.back_end_for_TTECH.shared.api.annotation.AuthUser;
import com.dino.back_end_for_TTECH.shared.api.model.CurrentUser;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/campaigns/coupons")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CouponController {

  CouponService service;

  @PostMapping("/coupon-claims/{id}")
  public ResponseEntity<?> claim(
      @AuthUser CurrentUser user,
      @PathVariable long id) {

    this.service.claim(user.id(), id);
    return ResponseEntity.ok(Map.of());
  }

  @DeleteMapping("/coupon-claims/{id}")
  public ResponseEntity<?> unclaim(
      @AuthUser CurrentUser user,
      @PathVariable long id) {

    this.service.unclaim(user.id(), id);
    return ResponseEntity.ok(Map.of());
  }

  @PostMapping("/coupon-previews")
  public ResponseEntity<?> preview(
      @AuthUser CurrentUser user,
      @RequestBody CouponBodyApply body) {

    var data = this.service.preview(user.id(), body);
    return ResponseEntity.ok(data);
  }

  @GetMapping
  public ResponseEntity<?> list(
      @AuthUser CurrentUser user,
      @RequestParam("product-id") Long productId) {

    var data = this.service.list(user.id(), productId);
    return ResponseEntity.ok(data);
  }

}
