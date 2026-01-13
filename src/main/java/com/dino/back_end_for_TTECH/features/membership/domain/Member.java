package com.dino.back_end_for_TTECH.features.membership.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.dino.back_end_for_TTECH.features.membership.domain.model.MemberStatus;
import com.dino.back_end_for_TTECH.features.profile.domain.User;
import com.dino.back_end_for_TTECH.shared.domain.model.BaseEntity;
import com.dino.back_end_for_TTECH.shared.domain.model.BaseStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "members")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Member extends BaseEntity implements BaseStatus<MemberStatus> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "members_seq")
  @SequenceGenerator(name = "members_seq", allocationSize = 6)
  @Column(name = "member_id")
  Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", unique = true, nullable = false)
  User customer;

  Integer points;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "membership_id", nullable = false)
  Membership membership;

  @Column(nullable = false)
  LocalDateTime rankedAt;

  @Column(nullable = false)
  String status;

  public void rank(int newPoints, List<Membership> membshipList) {
    if (membshipList == null || membshipList.isEmpty())
      return;

    membshipList.sort((a, b) -> b.getMinPoint().compareTo(a.getMinPoint()));
    Membership newMembship = membshipList.stream()
        .filter(m -> this.getPoints() >= m.getMinPoint())
        .findFirst()
        .orElse(membshipList.getLast());

    int oldLevel = this.getMembership().getMinPoint();
    int newLevel = newMembship.getMinPoint();
    MemberStatus newStatus = newLevel > oldLevel
        ? MemberStatus.UPGRADE
        : newLevel == oldLevel
            ? MemberStatus.RENEW
            : MemberStatus.DOWNGRADE;

    this.setPoints(newPoints);
    this.setMembership(newMembship);
    this.setRankedAt(LocalDateTime.now());
    this.setStatus(newStatus);
  }

  public boolean isEffective() {
    LocalDateTime expiryDate = rankedAt.plusMonths(membership.getValidityMonths());
    return expiryDate.isAfter(LocalDateTime.now());
  }
}