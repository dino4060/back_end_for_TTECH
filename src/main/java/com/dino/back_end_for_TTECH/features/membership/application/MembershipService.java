package com.dino.back_end_for_TTECH.features.membership.application;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dino.back_end_for_TTECH.features.membership.application.mapper.MembershipMapper;
import com.dino.back_end_for_TTECH.features.membership.application.model.BenefitBody;
import com.dino.back_end_for_TTECH.features.membership.application.model.MembershipBody;
import com.dino.back_end_for_TTECH.features.membership.application.model.MembershipData;
import com.dino.back_end_for_TTECH.features.membership.domain.Benefit;
import com.dino.back_end_for_TTECH.features.membership.domain.Membership;
import com.dino.back_end_for_TTECH.features.membership.domain.repository.MembershipRepository;
import com.dino.back_end_for_TTECH.shared.application.exception.NotFoundE;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class MembershipService {

  MembershipRepository membshipRepo;
  MembershipMapper membshipMapper;

  @Transactional(readOnly = true)
  public Membership findStarterRank() {
    var memberships = membshipRepo.findAll();

    if (memberships.isEmpty())
      return null;

    return Collections.min(memberships, (a, b) -> a.getMinPoint().compareTo(b.getMinPoint()));
  }

  @Transactional(readOnly = true)
  public List<MembershipData> list() {
    return membshipRepo.findAll().stream()
        .map(membshipMapper::toData)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public MembershipData get(long id) {
    var membership = membshipRepo.findWithBenefitsById(id).orElseThrow(() -> new NotFoundE("Membership not found"));

    return membshipMapper.toData(membership);
  }

  @Transactional
  public void delete(long id) {
    var membership = membshipRepo.findById(id).orElseThrow(() -> new NotFoundE("Membership not found"));
    membshipRepo.delete(membership);
  }

  @Transactional
  public MembershipData update(MembershipBody body) {
    var id = body.getId();
    var membership = membshipRepo.findWithBenefitsById(id).orElseThrow(() -> new NotFoundE("Membership not found"));

    membshipMapper.toModel(body, membership);

    if (body.getBenefits() != null) {
      processBenefits(membership, body.getBenefits());
    }

    var saved = membshipRepo.save(membership);
    return membshipMapper.toData(saved);
  }

  @Transactional
  public MembershipData create(MembershipBody body) {
    var membership = membshipMapper.toModel(body);

    if (body.getBenefits() != null && !body.getBenefits().isEmpty()) {
      List<Benefit> benefits = body.getBenefits().stream()
          .map(benefitBody -> createBenefit(membership, benefitBody))
          .collect(Collectors.toList());

      membership.getBenefits().addAll(benefits);
    }

    var saved = membshipRepo.save(membership);
    return membshipMapper.toData(saved);
  }

  private void processBenefits(Membership membership, List<BenefitBody> benefitBodies) {
    Map<Long, Benefit> existing = membership.getBenefits().stream()
        .filter(b -> b.getId() != null)
        .collect(Collectors.toMap(Benefit::getId, b -> b));

    List<Benefit> updated = benefitBodies.stream()
        .map(body -> {
          if (body.getId() != null && existing.containsKey(body.getId())) {
            Benefit benefit = existing.remove(body.getId());
            updateBenefit(benefit, body);
            return benefit;
          } else {
            return createBenefit(membership, body);
          }
        })
        .collect(Collectors.toList());

    membership.getBenefits().clear();
    membership.getBenefits().addAll(updated);
  }

  private Benefit createBenefit(Membership membership, BenefitBody body) {
    var benefit = new Benefit();
    benefit.setMembership(membership);
    benefit.setBenefitType(body.getBenefitType());
    benefit.setBenefitName(body.getBenefitName());
    benefit.setBenefitValue(body.getBenefitValue());
    benefit.setBenefitUnit(body.getBenefitUnit());
    benefit.setMinSpend(body.getMinSpend());
    benefit.setValidityMonths(body.getValidityMonths());
    benefit.setLimitPerCustomer(body.getLimitPerCustomer());
    return benefit;
  }

  private void updateBenefit(Benefit benefit, BenefitBody body) {
    benefit.setBenefitType(body.getBenefitType());
    benefit.setBenefitName(body.getBenefitName());
    benefit.setBenefitValue(body.getBenefitValue());
    benefit.setBenefitUnit(body.getBenefitUnit());
    benefit.setMinSpend(body.getMinSpend());
    benefit.setValidityMonths(body.getValidityMonths());
    benefit.setLimitPerCustomer(body.getLimitPerCustomer());
  }

}
