package com.dino.back_end_for_TTECH.features.profile.application.model;

import com.dino.back_end_for_TTECH.features.membership.application.model.MemberData;
import com.dino.back_end_for_TTECH.features.profile.domain.model.Role;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerData {
  Long id;
  String name;
  String username;
  String email;
  String phone;

  Set<Role> roles;
  Instant createdAt;
  Instant updatedAt;
  MemberData member;
}
