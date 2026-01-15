package com.dino.back_end_for_TTECH.features.product.application.model;

import java.util.ArrayList;
import java.util.List;

import com.dino.back_end_for_TTECH.features.product.domain.model.ProductSpecification;
import com.dino.back_end_for_TTECH.features.promotion.application.model.CampaignData;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDataFull {
  long id;
  String name;
  String thumb;
  String version;
  String color;
  String status;

  List<String> photos;
  String description;
  int guaranteeMonths;

  PriceData price;
  StockData stock;
  CategoryData category;
  SeriesData series;

  List<ProductSpecification> specifications = new ArrayList<>();

  CampaignData discountCampaign;
}
