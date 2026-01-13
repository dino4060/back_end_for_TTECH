package com.dino.back_end_for_TTECH.features.membership.application;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dino.back_end_for_TTECH.features.membership.application.mapper.MembershipMapper;
import com.dino.back_end_for_TTECH.features.membership.application.model.MembershipData;
import com.dino.back_end_for_TTECH.features.membership.domain.Benefit;
import com.dino.back_end_for_TTECH.features.membership.domain.Member;
import com.dino.back_end_for_TTECH.features.membership.domain.Membership;
import com.dino.back_end_for_TTECH.features.membership.domain.model.BenefitType;
import com.dino.back_end_for_TTECH.features.membership.domain.model.MemberStatus;
import com.dino.back_end_for_TTECH.features.membership.domain.repository.MemberRepository;
import com.dino.back_end_for_TTECH.features.membership.domain.repository.MembshipRepository;
import com.dino.back_end_for_TTECH.features.profile.domain.User;
import com.dino.back_end_for_TTECH.features.profile.domain.repository.UserRepository;
import com.dino.back_end_for_TTECH.shared.application.exception.NotFoundE;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

  MemberRepository memberRepo;

  MembshipRepository membshipRepo;
  MembershipMapper membshipMapper;

  UserRepository userRepo;

  public List<Benefit> offerBenefits(Member member) {
    var membership = this.offerMembership(member);

    var benefits = membership.getBenefits();

    if (member.hasStatus(MemberStatus.UPGRADE)) {
      return benefits.stream()
          .filter(b -> !BenefitType.RENEW.name().equals(b.getBenefitType()))
          .toList();
    }
    if (member.hasStatus(MemberStatus.RENEW)) {
      return benefits.stream()
          .filter(b -> !BenefitType.UPGRADE.name().equals(b.getBenefitType()))
          .toList();
    }
    if (member.hasStatus(MemberStatus.DOWNGRADE)) {
      return benefits.stream()
          .filter(b -> !BenefitType.UPGRADE.name().equals(b.getBenefitType()))
          .filter(b -> !BenefitType.RENEW.name().equals(b.getBenefitType()))
          .toList();
    }
    return List.of();
  }

  public Membership offerMembership(Member member) {
    if (member.isEffective()) {
      return member.getMembership();
    }

    LocalDateTime fromLocal = LocalDateTime.now().minusMonths(member.getMembership().getValidityMonths());
    Instant from = fromLocal.toInstant(ZoneOffset.UTC);
    int points = memberRepo.calcPointsByCustomerFrom(member.getCustomer(), from);

    var memberships = membshipRepo.findAll();
    if (memberships.isEmpty()) {
      return null;
    }

    member.rank(points, memberships);
    memberRepo.save(member);
    return member.getMembership();
  }

  public MembershipData offerMembership(Long customerId) {
    Member member = this.findOrCreate(customerId);
    if (member == null) {
      return membshipMapper.toData(null);
    }

    var membership = this.offerMembership(member);
    if (membership == null) {
      return membshipMapper.toData(null);
    }

    var data = membshipMapper.toData(membership);
    data.setPoints(member.getPoints());
    return data;
  }

  public void plusPoints(Long customerId, int total) {
    var member = this.findOrCreate(customerId);

    LocalDateTime fromLocal = LocalDateTime.now().minusMonths(member.getMembership().getValidityMonths());
    Instant from = fromLocal.toInstant(ZoneOffset.UTC);
    int points = memberRepo.calcPointsByCustomerFrom(member.getCustomer(), from) + total;

    var memberships = membshipRepo.findAll();
    if (memberships.isEmpty()) {
      return;
    }

    member.rank(points, memberships);
    memberRepo.save(member);
    return;
  }

  public void minusPoints(Long customerId, int total) {
    var member = this.findOrCreate(customerId);

    LocalDateTime fromLocal = LocalDateTime.now().minusMonths(member.getMembership().getValidityMonths());
    Instant from = fromLocal.toInstant(ZoneOffset.UTC);
    int points = memberRepo.calcPointsByCustomerFrom(member.getCustomer(), from) - total;

    var memberships = membshipRepo.findAll();
    if (memberships.isEmpty()) {
      return;
    }

    member.rank(points, memberships);
    memberRepo.save(member);
    return;
  }

  public Member findOrCreate(Long customerId) {
    if (customerId == null) {
      throw new NotFoundE("Customer not found");
    }

    User customer = userRepo.findById(customerId).orElseThrow(() -> new NotFoundE("Customer not found"));

    Member member = memberRepo.findByCustomer(customer).orElse(null);
    if (member == null) {
      var memberships = membshipRepo.findAll();
      if (memberships.isEmpty()) {
        return null;
      }

      var newMember = new Member();
      newMember.setCustomer(customer);
      newMember.setPoints(0);
      newMember.setMembership(memberships.getLast());
      newMember.setRankedAt(LocalDateTime.now());
      newMember.setStatus(MemberStatus.UPGRADE);
      var saved = this.memberRepo.save(newMember);
      return saved;
    }

    return member;
  }
}
