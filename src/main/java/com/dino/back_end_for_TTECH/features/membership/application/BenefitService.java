package com.dino.back_end_for_TTECH.features.membership.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dino.back_end_for_TTECH.features.membership.domain.model.ApplyResult;
import com.dino.back_end_for_TTECH.features.membership.domain.model.BenefitType;
import com.dino.back_end_for_TTECH.features.membership.domain.repository.BenefitRepository;
import com.dino.back_end_for_TTECH.features.profile.domain.repository.UserRepository;
import com.dino.back_end_for_TTECH.features.promotion.application.model.CouponBodyApply;
import com.dino.back_end_for_TTECH.shared.api.model.CurrentUser;
import com.dino.back_end_for_TTECH.shared.application.exception.NotFoundE;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class BenefitService {

  BenefitRepository benefitRepo;

  MembshipService membshipService;

  UserRepository userRepo;

  @Transactional(readOnly = true)
  public List<ApplyResult> preview(
      long customerId,
      CouponBodyApply body) {

    var customer = userRepo.findById(customerId).orElseThrow(() -> new NotFoundE("Customer not found"));

    var membership = this.membshipService.offerTo(customer);
    if (membership == null) {
      return List.of();
    }

    var benefits = this.benefitRepo.findByMembership(membership);
    if (benefits.size() == 0) {
      return List.of();
    }

    var appliedResults = benefits.stream()
        .map(b -> b.canApply(customerId, body.getSpendAmount()))
        .filter(r -> r.getIsApplied())
        .collect(Collectors.toList());

    var bestUp = findBest(appliedResults, BenefitType.UP);
    var bestRenew = findBest(appliedResults, BenefitType.RENEW);
    var bestCoupon = findBest(appliedResults, BenefitType.COUPON);
    var bestGuarantee = findBest(appliedResults, BenefitType.GUARANTEE);

    return List.of(bestUp, bestRenew, bestCoupon, bestGuarantee);
  }

  private ApplyResult findBest(List<ApplyResult> results, BenefitType type) {
    return results.stream()
        .filter(r -> type.toString().equals(r.getBenefitType()))
        .max(Comparator.comparing(ApplyResult::getBenefitValue))
        .orElseGet(() -> ApplyResult.fail(String.format("No %s benefit is applied", type.toString())));
  }

  @Transactional
  public void apply(
      CurrentUser customer,
      int spendAmount) {

    return;
  }
}
