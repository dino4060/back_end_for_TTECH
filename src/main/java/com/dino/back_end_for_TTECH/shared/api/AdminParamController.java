package com.dino.back_end_for_TTECH.shared.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dino.back_end_for_TTECH.shared.api.annotation.AuthUser;
import com.dino.back_end_for_TTECH.shared.api.model.CurrentUser;
import com.dino.back_end_for_TTECH.shared.application.ParamService;
import com.dino.back_end_for_TTECH.shared.application.model.ParamBody;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/admin/params")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminParamController {

  ParamService paramService;

  @GetMapping("/memberships")
  public ResponseEntity<?> getMembership(@AuthUser CurrentUser admin) {
    var data = paramService.getMembership();
    return ResponseEntity.ok(data);
  }

  @PatchMapping("/memberships")
  public ResponseEntity<?> patchMembership(
      @AuthUser CurrentUser admin,
      @Valid @RequestBody ParamBody body) {

    var data = paramService.patchMembership(body);
    return ResponseEntity.ok(data);
  }
}