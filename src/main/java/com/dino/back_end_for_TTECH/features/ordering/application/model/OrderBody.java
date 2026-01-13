package com.dino.back_end_for_TTECH.features.ordering.application.model;

import java.util.List;

import com.dino.back_end_for_TTECH.features.membership.domain.model.ApplyResult;
import com.dino.back_end_for_TTECH.features.promotion.domain.model.CouponApplyResult;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderBody {
  int allPrice = 0;
  int allDiscount = 0;
  int shippingFee = 0;
  int total = 0;

  String note = "";
  String paymentType = "COD";
  List<String> giftTexts;

  String toUserName;
  String toPhone;
  Integer toProvinceId;
  Integer toWardId;
  String toStreet;

  String fromUserName;
  String fromPhone;
  Integer fromProvinceId;
  Integer fromWardId;
  String fromStreet;

  List<CouponApplyResult> couponResults;
  List<ApplyResult> benefitResults;
}
