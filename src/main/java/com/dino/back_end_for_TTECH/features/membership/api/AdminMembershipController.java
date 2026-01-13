package com.dino.back_end_for_TTECH.features.membership.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dino.back_end_for_TTECH.features.membership.application.MembshipService;
import com.dino.back_end_for_TTECH.features.membership.application.model.MembershipBody;
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

  MembshipService membshipService;

  @GetMapping
  public ResponseEntity<?> list() {
    var data = membshipService.list();
    return ResponseEntity.ok(data);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> get(@PathVariable long id) {

    var data = membshipService.get(id);
    return ResponseEntity.ok(data);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable long id) {

    membshipService.delete(id);
    return ResponseEntity.ok().build();
  }

  @PutMapping
  public ResponseEntity<?> update(@Valid @RequestBody MembershipBody body) {

    var data = membshipService.update(body);
    return ResponseEntity.ok(data);
  }

  @PostMapping
  public ResponseEntity<?> create(@Valid @RequestBody MembershipBody body) {

    var data = membshipService.create(body);
    return ResponseEntity.ok(data);
  }
}
