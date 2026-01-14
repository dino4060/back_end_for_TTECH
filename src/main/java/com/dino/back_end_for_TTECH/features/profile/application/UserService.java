package com.dino.back_end_for_TTECH.features.profile.application;

import com.dino.back_end_for_TTECH.features.membership.application.MemberService;
import com.dino.back_end_for_TTECH.features.profile.application.mapper.UserMapper;
import com.dino.back_end_for_TTECH.features.profile.application.model.CustomerData;
import com.dino.back_end_for_TTECH.features.profile.application.model.UserBody;
import com.dino.back_end_for_TTECH.features.profile.application.model.UserData;
import com.dino.back_end_for_TTECH.features.profile.application.model.UserQuery;
import com.dino.back_end_for_TTECH.features.profile.domain.User;
import com.dino.back_end_for_TTECH.features.profile.domain.repository.UserRepository;
import com.dino.back_end_for_TTECH.shared.api.model.CurrentUser;
import com.dino.back_end_for_TTECH.shared.application.exception.DuplicationE;
import com.dino.back_end_for_TTECH.shared.application.exception.NotFoundE;
import com.dino.back_end_for_TTECH.shared.application.model.PageData;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

  UserRepository userRepository;
  UserMapper userMapper;

  MemberService memberService;

  public UserData edit(UserBody body, CurrentUser currentUser) {
    User user = this.userRepository
        .findById(currentUser.id())
        .orElseThrow(() -> new NotFoundE("User not found"));

    if (!body.getEmail().equals(user.getEmail())) {
      this.userRepository
          .findByEmail(body.getEmail())
          .ifPresent(existingUser -> {
            if (!existingUser.getId().equals(currentUser.id())) {
              throw new DuplicationE("Email already exists");
            }
          });
    }

    if (!body.getPhone().equals(user.getPhone())) {
      this.userRepository
          .findByPhone(body.getPhone())
          .ifPresent(existingUser -> {
            if (!existingUser.getId().equals(currentUser.id())) {
              throw new DuplicationE("Phone already exists.");
            }
          });
    }

    this.userMapper.toModel(body, user);
    User editUser = userRepository.save(user);
    return this.userMapper.toData(editUser);
  }

  public PageData<CustomerData> listCustomers(UserQuery query) {
    var page = this.userRepository.findAll(
        this.userMapper.toQueryable(query),
        this.userMapper.toPageable(query));

    var memberMap = memberService.findOrCreate(page.getContent());

    return this.userMapper.toPageData(
        page,
        user -> this.userMapper.toCustomerData(
            user,
            this.userMapper.toMemberData(memberMap.get(user.getId()))));
  }
}
