package com.dino.back_end_for_TTECH.features.membership.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.dino.back_end_for_TTECH.features.membership.domain.Membership;
import com.dino.back_end_for_TTECH.features.membership.domain.repository.MembshipRepository;
import com.dino.back_end_for_TTECH.features.profile.domain.User;
import com.dino.back_end_for_TTECH.features.profile.domain.repository.UserRepository;
import com.dino.back_end_for_TTECH.features.promotion.domain.model.CouponApplyResult;
import com.dino.back_end_for_TTECH.shared.api.annotation.AuthUser;
import com.dino.back_end_for_TTECH.shared.api.model.CurrentUser;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class MembshipService {

  MembshipRepository membershipRepo;
  UserRepository userRepo;

  @Transactional
  public Membership offerTo(User customer) {
    Sort descPriority = Sort.by(Sort.Direction.DESC, "minPoint");
    List<Membership> memberships = membershipRepo.findAll(descPriority);

    if (memberships.isEmpty()) {
      return null;
    }

    LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
    Integer points = userRepo.sumPaymentByCustomerSince(customer, sixMonthsAgo);

    if (points == null || points <= 0) {
      return memberships.getLast();
    }

    Membership offerMembership = memberships.stream()
        .filter(m -> points >= m.getMinPoint())
        .findFirst()
        .orElse(memberships.getLast());

    return offerMembership;
  }

  @Transactional
  public void getByCustomer(
      @AuthUser CurrentUser customer,
      @RequestBody CouponApplyResult appliedCoupon) {

    return;
  }

  @Transactional
  public void listCustomer(
      @AuthUser CurrentUser customer,
      @RequestBody CouponApplyResult appliedCoupon) {

    return;
  }

}
