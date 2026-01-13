package com.dino.back_end_for_TTECH.features.membership.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dino.back_end_for_TTECH.features.membership.application.model.ApplyBody;
import com.dino.back_end_for_TTECH.features.membership.domain.Benefit;
import com.dino.back_end_for_TTECH.features.membership.domain.Member;
import com.dino.back_end_for_TTECH.features.membership.domain.model.ApplyResult;
import com.dino.back_end_for_TTECH.features.membership.domain.model.BenefitType;
import com.dino.back_end_for_TTECH.features.membership.domain.repository.BenefitRepository;
import com.dino.back_end_for_TTECH.shared.api.model.CurrentUser;
import com.dino.back_end_for_TTECH.shared.application.exception.BadRequestE;

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

  MemberService memberService;

  @Transactional
  public void apply(
      CurrentUser customer,
      ApplyResult benefitResult) {

    if (!benefitResult.getIsApplied()) {
      return;
    }

    Benefit benefit = benefitRepo.findById(benefitResult.getId())
        .orElseThrow(() -> new BadRequestE("Benefit not found"));

    benefit.markAsUsed(customer.id());
  }

  public List<ApplyResult> preview(
      Long customerId,
      ApplyBody applyBody) {

    Member member = memberService.findOrCreate(customerId);
    if (member == null) {
      return List.of();
    }

    var benefits = this.memberService.offerBenefits(member);
    if (benefits.isEmpty()) {
      return List.of();
    }

    var appliedResults = benefits.stream()
        .map(b -> b.canApply(customerId, applyBody.getSpendAmount()))
        .filter(r -> r.getIsApplied())
        .peek(r -> r.setMembershipCode(member.getMembership().getMembershipCode()))
        .collect(Collectors.toList());

    var bestUpgrade = findBest(appliedResults, BenefitType.UPGRADE);
    var bestRenew = findBest(appliedResults, BenefitType.RENEW);
    var bestCoupon = findBest(appliedResults, BenefitType.COUPON);
    var bestGuarantee = findBest(appliedResults, BenefitType.GUARANTEE);

    return List.of(bestUpgrade, bestRenew, bestCoupon, bestGuarantee);
  }

  private ApplyResult findBest(List<ApplyResult> results, BenefitType type) {
    return results.stream()
        .peek((r) -> {
          System.out.println(type.name());
          System.out.println(r.getBenefitType());
          System.out.println(type.name().equals(r.getBenefitType()));
        })
        .filter(r -> type.name().equals(r.getBenefitType()))
        .max(Comparator.comparing(ApplyResult::getBenefitValue))
        .orElseGet(() -> ApplyResult.fail(String.format("No %s benefit is applied", type.name())));
  }
}
