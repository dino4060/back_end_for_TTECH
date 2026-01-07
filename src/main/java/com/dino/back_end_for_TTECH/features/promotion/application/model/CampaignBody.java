package com.dino.back_end_for_TTECH.features.promotion.application.model;

import java.time.LocalDateTime;

import com.dino.back_end_for_TTECH.shared.application.utils.BaseBody;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = false)
@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
public class CampaignBody extends BaseBody {

  String promotionType;

  String name;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  LocalDateTime startTime;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  LocalDateTime endTime;
}
