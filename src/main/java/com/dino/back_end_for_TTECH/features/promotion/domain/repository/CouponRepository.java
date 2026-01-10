package com.dino.back_end_for_TTECH.features.promotion.domain.repository;

import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.dino.back_end_for_TTECH.features.promotion.domain.Coupon;
import com.dino.back_end_for_TTECH.shared.domain.BaseRepository;

@Repository
public interface CouponRepository extends BaseRepository<Coupon, Long> {

  @Override
  default String customModelName() {
    return Coupon.class.getSimpleName();
  }

  Optional<Coupon> findByCouponCode(@NonNull String couponCode);

}
