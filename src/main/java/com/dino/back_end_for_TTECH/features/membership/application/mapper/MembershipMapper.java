package com.dino.back_end_for_TTECH.features.membership.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.dino.back_end_for_TTECH.features.membership.application.model.BenefitData;
import com.dino.back_end_for_TTECH.features.membership.application.model.MembershipBody;
import com.dino.back_end_for_TTECH.features.membership.application.model.MembershipData;
import com.dino.back_end_for_TTECH.features.membership.domain.Benefit;
import com.dino.back_end_for_TTECH.features.membership.domain.Membership;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MembershipMapper {

  @Mapping(target = "benefits", ignore = true)
  Membership toModel(MembershipBody body);

  @Mapping(target = "benefits", ignore = true)
  void toModel(MembershipBody body, @MappingTarget Membership model);

  MembershipData toData(Membership model);

  BenefitData toBenefitData(Benefit benefit);
}