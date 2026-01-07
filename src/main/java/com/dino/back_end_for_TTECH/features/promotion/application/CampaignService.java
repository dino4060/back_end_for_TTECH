package com.dino.back_end_for_TTECH.features.promotion.application;

import org.springframework.stereotype.Service;

import com.dino.back_end_for_TTECH.features.promotion.application.mapper.CampaignMapper;
import com.dino.back_end_for_TTECH.features.promotion.application.model.CampaignData;
import com.dino.back_end_for_TTECH.features.promotion.application.model.CampaignQuery;
import com.dino.back_end_for_TTECH.features.promotion.domain.Campaign;
import com.dino.back_end_for_TTECH.features.promotion.domain.Coupon;
import com.dino.back_end_for_TTECH.features.promotion.domain.Sale;
import com.dino.back_end_for_TTECH.features.promotion.domain.repository.CampaignRepository;
import com.dino.back_end_for_TTECH.shared.application.exception.BadRequestE;
import com.dino.back_end_for_TTECH.shared.application.model.PageData;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CampaignService {

  CampaignRepository campaignRepo;
  CampaignMapper campaignMapper;

  CampaignHelper campaignSharedService;
  SaleService discountService;
  CouponService couponService;

  public void delete(long id) {
    var model = this.campaignRepo.getById(id);

    // this.campaignRepo.delete(model);

    if (model instanceof Sale discount) {
      this.discountService.delete(discount);
      return;
    }

    if (model instanceof Coupon coupon) {
      this.couponService.delete(coupon);
      return;
    }

    throw new BadRequestE("Campaign.promotionGroup is out of scope: " + model.getPromotionGroup());
  }

  public PageData<CampaignData> list(CampaignQuery query) {
    var page = this.campaignRepo.findAll(
        this.campaignMapper.toQueryable(query),
        this.campaignMapper.toPageable(query));

    for (var campaign : page.getContent()) {
      this.campaignSharedService.saveSyncStatus(campaign);
    }

    return this.campaignMapper.toPageData(
        page, (Campaign c) -> this.campaignMapper.toData(c));
  }
}
