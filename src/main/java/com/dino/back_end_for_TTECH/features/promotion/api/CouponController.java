package com.dino.back_end_for_TTECH.features.promotion.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

  @PostMapping("/preview")
  public ResponseEntity<?> preview(
      @AuthUser CurrentUser user,
      @RequestBody CouponBodyApply body) {

    var data = this.service.preview(user, body);
    return ResponseEntity.ok(data);
  }

}
