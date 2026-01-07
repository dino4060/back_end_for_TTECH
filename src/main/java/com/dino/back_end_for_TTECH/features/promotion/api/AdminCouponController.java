package com.dino.back_end_for_TTECH.features.promotion.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  CouponService couponService;

  @GetMapping("/{id}")
  public ResponseEntity<?> get(
      @PathVariable long id) {

    var data = this.couponService.get(id);
    return ResponseEntity.ok(data);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(
      @PathVariable long id) {

    this.couponService.delete(id);
    return ResponseEntity.ok(Map.of());
  }

  @PatchMapping
  public ResponseEntity<?> patch(
      @Valid @RequestBody CouponBodyPatch body) {

    var result = this.couponService.patch(body);
    return ResponseEntity.ok(result);
  }

  @PutMapping
  public ResponseEntity<?> update(
      @Valid @RequestBody CouponBodyUpdate body) {

    var result = this.couponService.update(body);
    return ResponseEntity.ok(result);
  }

  @PostMapping
  public ResponseEntity<?> create(
      @Valid @RequestBody CouponBody body) {

    var result = this.couponService.create(body);
    return ResponseEntity.ok(result);
  }
}
