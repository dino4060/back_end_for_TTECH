package com.dino.back_end_for_TTECH.features.promotion.domain.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;

import com.dino.back_end_for_TTECH.features.profile.domain.User;
import com.dino.back_end_for_TTECH.features.promotion.domain.Coupon;
import com.dino.back_end_for_TTECH.features.promotion.domain.CustomerCoupon;
import com.dino.back_end_for_TTECH.shared.domain.BaseRepository;

public interface CustomerCouponRepository extends BaseRepository<CustomerCoupon, Long> {

  Optional<CustomerCoupon> findByCustomerAndCoupon(User customer, Coupon coupon);

  boolean existsByCustomerAndCoupon(User customer, Coupon coupon);

  boolean existsByCoupon(Coupon coupon);

  @EntityGraph(attributePaths = { "coupon" })
  Set<CustomerCoupon> findAllByCustomer(User customer);

  @Override
  default String customModelName() {
    return CustomerCoupon.class.getName();
  }
}