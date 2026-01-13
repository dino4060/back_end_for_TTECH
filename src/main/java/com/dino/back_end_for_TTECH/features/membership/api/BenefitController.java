package com.dino.back_end_for_TTECH.features.membership.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dino.back_end_for_TTECH.features.membership.application.BenefitService;
import com.dino.back_end_for_TTECH.features.membership.application.model.ApplyBody;
import com.dino.back_end_for_TTECH.shared.api.annotation.AuthUser;
import com.dino.back_end_for_TTECH.shared.api.model.CurrentUser;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/benefits")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BenefitController {

  BenefitService benefitService;

  @PostMapping("/benefit-previews")
  public ResponseEntity<?> preview(
      @AuthUser CurrentUser customer,
      @RequestBody @Valid ApplyBody body) {

    var data = this.benefitService.preview(customer.id(), body);
    return ResponseEntity.ok(data);
  }
}