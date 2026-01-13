package com.dino.back_end_for_TTECH.features.membership.domain.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dino.back_end_for_TTECH.features.membership.domain.Benefit;
import com.dino.back_end_for_TTECH.features.membership.domain.Membership;
import com.dino.back_end_for_TTECH.shared.domain.BaseRepository;

@Repository
public interface BenefitRepository extends BaseRepository<Benefit, Long> {
  List<Benefit> findByMembership(Membership membership);
}
