package com.dino.back_end_for_TTECH.features.promotion.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dino.back_end_for_TTECH.features.promotion.application.CouponService;
import com.dino.back_end_for_TTECH.features.promotion.application.model.CouponBody;
import com.dino.back_end_for_TTECH.features.promotion.application.model.CouponBodyPatch;
import com.dino.back_end_for_TTECH.features.promotion.application.model.CouponBodyUpdate;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/admin/campaigns/coupons")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCouponController {

  CouponService service;

  @GetMapping("/{id}")
  public ResponseEntity<?> get(
      @PathVariable long id) {

    var data = this.service.get(id);
    return ResponseEntity.ok(data);
  }

  @PatchMapping
  public ResponseEntity<?> patch(
      @Valid @RequestBody CouponBodyPatch body) {

    var result = this.service.patch(body);
    return ResponseEntity.ok(result);
  }

  @PutMapping
  public ResponseEntity<?> update(
      @Valid @RequestBody CouponBodyUpdate body) {

    var result = this.service.update(body);
    return ResponseEntity.ok(result);
  }

  @PostMapping
  public ResponseEntity<?> create(
      @Valid @RequestBody CouponBody body) {

    var result = this.service.create(body);
    return ResponseEntity.ok(result);
  }
}
