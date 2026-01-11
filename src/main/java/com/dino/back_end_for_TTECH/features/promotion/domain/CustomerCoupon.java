package com.dino.back_end_for_TTECH.features.promotion.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.dino.back_end_for_TTECH.features.profile.domain.User;
import com.dino.back_end_for_TTECH.shared.domain.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "customer_coupons", uniqueConstraints = @UniqueConstraint(columnNames = { "customer_id", "coupon_id" }))
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerCoupon extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_coupons_seq")
  @SequenceGenerator(name = "customer_coupons_seq", allocationSize = 1)
  Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  User customer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coupon_id", nullable = false)
  Coupon coupon;

  LocalDateTime claimedAt;

  LocalDateTime expiresAt;
}