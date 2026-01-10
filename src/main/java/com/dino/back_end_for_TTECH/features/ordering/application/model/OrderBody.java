package com.dino.back_end_for_TTECH.features.ordering.application.model;

import com.dino.back_end_for_TTECH.features.promotion.domain.model.CouponApplyResult;

import lombok.Data;

@Data
public class OrderBody {
  int allPrice = 0;
  int allDiscount = 0;
  int shippingFee = 0;
  int total = 0;

  String note = "";
  String paymentType = "COD";
  CouponApplyResult couponCode;

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
}
