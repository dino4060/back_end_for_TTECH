package com.dino.back_end_for_TTECH.features.membership.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.dino.back_end_for_TTECH.features.membership.domain.model.MemberStatus;
import com.dino.back_end_for_TTECH.features.profile.domain.User;
import com.dino.back_end_for_TTECH.shared.domain.model.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "memberships")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Membership extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "memberships_seq")
  @SequenceGenerator(name = "memberships_seq", allocationSize = 6)
  @Column(name = "membership_id")
  Long id;

  String membershipName;

  String membershipCode;

  Integer minPoint;

  Integer validityMonths;

  Boolean isAlive = true;

  @OneToMany(mappedBy = "membership", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  List<Benefit> benefits = new ArrayList<>();

  public Member enrollMember(User customer) {
    var member = new Member();
    member.setMembership(this);
    member.setCustomer(customer);
    member.setPoints(0);
    member.setRankedAt(LocalDateTime.now());
    member.setStatus(MemberStatus.UPGRADE);
    return member;
  }
}
