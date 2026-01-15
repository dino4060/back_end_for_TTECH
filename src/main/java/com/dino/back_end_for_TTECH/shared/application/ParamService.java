package com.dino.back_end_for_TTECH.shared.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dino.back_end_for_TTECH.shared.application.mapper.ParamMapper;
import com.dino.back_end_for_TTECH.shared.application.model.ParamBody;
import com.dino.back_end_for_TTECH.shared.application.model.ParamData;
import com.dino.back_end_for_TTECH.shared.domain.MembershipParam;
import com.dino.back_end_for_TTECH.shared.domain.Param;
import com.dino.back_end_for_TTECH.shared.domain.repository.ParamRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ParamService {

  ParamRepository paramRepo;
  ParamMapper paramMapper;

  public ParamData getMembership() {
    var param = paramRepo.findById(1).orElse(null);
    if (param == null) {
      var newParam = new Param();
      newParam.setId(1);

      var membership = new MembershipParam();
      membership.setValidityMonths(6);
      newParam.setMembership(membership);

      var savedParam = paramRepo.save(newParam);
      return paramMapper.toData(savedParam.getMembership());
    }
    return paramMapper.toData(param.getMembership());

  }

  @Transactional
  public ParamData patchMembership(ParamBody body) {
    var param = paramRepo.getOrCreate();

    if (body.getValidityMonths() != null) {
      param.getMembership().setValidityMonths(body.getValidityMonths());
      paramRepo.save(param);
    }

    return paramMapper.toData(param.getMembership());
  }
}
