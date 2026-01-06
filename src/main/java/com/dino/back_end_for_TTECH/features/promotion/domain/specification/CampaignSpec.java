package com.dino.back_end_for_TTECH.features.promotion.domain.specification;

import com.dino.back_end_for_TTECH.features.promotion.application.model.CampaignQuery;
import com.dino.back_end_for_TTECH.features.promotion.domain.Campaign;
import org.springframework.data.jpa.domain.Specification;

public class CampaignSpec {

    public static Specification<Campaign> build(CampaignQuery query) {
        return Specification.where(null);
    }
}
