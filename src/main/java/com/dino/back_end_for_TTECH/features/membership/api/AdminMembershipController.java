package com.dino.back_end_for_TTECH.features.membership.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dino.back_end_for_TTECH.features.membership.application.MembershipService;
import com.dino.back_end_for_TTECH.features.membership.application.model.MembershipBody;
import com.dino.back_end_for_TTECH.features.membership.application.model.MembershipBodyPatch;
import com.dino.back_end_for_TTECH.shared.api.constant.HasRole;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/admin/memberships")
@PreAuthorize(HasRole.ADMIN)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminMembershipController {

  MembershipService membershipService;

  @GetMapping
  public ResponseEntity<?> list() {
    var data = membershipService.list();
    return ResponseEntity.ok(data);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable long id) {

    membershipService.delete(id);
    return ResponseEntity.ok(Map.of());
  }

  @PatchMapping
  public ResponseEntity<?> patch(@RequestBody MembershipBodyPatch body) {

    membershipService.patch(body);
    return ResponseEntity.ok(Map.of());
  }

  @PutMapping
  public ResponseEntity<?> update(@Valid @RequestBody MembershipBody body) {

    var data = membershipService.update(body);
    return ResponseEntity.ok(data);
  }

  @PostMapping
  public ResponseEntity<?> create(@Valid @RequestBody MembershipBody body) {

    var data = membershipService.create(body);
    return ResponseEntity.ok(data);
  }
}
