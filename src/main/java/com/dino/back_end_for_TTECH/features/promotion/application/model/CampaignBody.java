package com.dino.back_end_for_TTECH.features.promotion.application.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = false)
@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
public class CampaignBody {

  @NotNull(message = "CampaignBody.promotionType is required")
  String promotionType;

  String name;

  @NotNull(message = "CampaignBody.startTime is required")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  LocalDateTime startTime;

  @NotNull(message = "CampaignBody.endTime is required")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  LocalDateTime endTime;
}
