package com.dino.back_end_for_TTECH.features.promotion.domain.repository;

import org.springframework.stereotype.Repository;

import com.dino.back_end_for_TTECH.features.promotion.domain.Campaign;
import com.dino.back_end_for_TTECH.shared.domain.BaseRepository;

@Repository
public interface CampaignRepository extends BaseRepository<Campaign, Long> {

  @Override
  default String customModelName() {
    return Campaign.class.getSimpleName();
  }

}