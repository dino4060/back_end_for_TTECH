package com.dino.back_end_for_TTECH.features.membership.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import com.dino.back_end_for_TTECH.features.membership.domain.Membership;
import com.dino.back_end_for_TTECH.shared.domain.BaseRepository;

@Repository
public interface MembshipRepository extends BaseRepository<Membership, Long> {
  @EntityGraph(attributePaths = { "benefits" })
  Optional<Membership> findWithBenefitsById(Long id);
}
