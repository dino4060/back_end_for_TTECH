package com.dino.back_end_for_TTECH.shared.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.dino.back_end_for_TTECH.shared.application.model.ParamData;
import com.dino.back_end_for_TTECH.shared.domain.MembershipParam;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParamMapper {

  ParamData toData(MembershipParam membership);
}