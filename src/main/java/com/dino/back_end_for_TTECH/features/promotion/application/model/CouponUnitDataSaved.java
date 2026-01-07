package com.dino.back_end_for_TTECH.features.promotion.application.model;

import com.dino.back_end_for_TTECH.shared.application.utils.ObjectId;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CouponUnitDataSaved {

  Long id;

  ObjectId product;
}
