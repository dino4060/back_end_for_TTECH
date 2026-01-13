package com.dino.back_end_for_TTECH.features.membership.domain.repository;

import org.springframework.stereotype.Repository;

import com.dino.back_end_for_TTECH.features.membership.domain.Membership;
import com.dino.back_end_for_TTECH.shared.domain.BaseRepository;

@Repository
public interface MembshipRepository extends BaseRepository<Membership, Long> {

}
