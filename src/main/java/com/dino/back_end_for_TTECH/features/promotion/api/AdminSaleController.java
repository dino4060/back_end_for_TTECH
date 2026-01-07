package com.dino.back_end_for_TTECH.features.promotion.api;

import com.dino.back_end_for_TTECH.features.promotion.application.SaleService;
import com.dino.back_end_for_TTECH.features.promotion.application.model.SaleBody;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/campaigns")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminSaleController {

  SaleService service;

  @GetMapping("/sales/{id}")
  public ResponseEntity<?> get(
      @PathVariable long id) {

    var data = this.service.get(id);
    return ResponseEntity.ok(data);
  }

  @PostMapping("/sales")
  public ResponseEntity<?> create(
      @Valid @RequestBody SaleBody body) {

    this.service.create(body);
    return ResponseEntity.ok(Map.of());
  }

  @PutMapping("/sales/{id}")
  public ResponseEntity<?> update(
      @PathVariable long id,
      @Valid @RequestBody SaleBody body) {

    this.service.update(id, body);
    return ResponseEntity.ok(Map.of());
  }
}
