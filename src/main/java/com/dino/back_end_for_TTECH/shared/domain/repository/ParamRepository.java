package com.dino.back_end_for_TTECH.shared.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dino.back_end_for_TTECH.shared.domain.MembershipParam;
import com.dino.back_end_for_TTECH.shared.domain.Param;

public interface ParamRepository extends JpaRepository<Param, Integer> {

  default Param getOrCreate() {
    Optional<Param> param = findById(1);

    if (param.isPresent()) {
      return param.get();
    }

    var newParam = new Param();
    newParam.setId(1);

    var membership = new MembershipParam();
    membership.setValidityMonths(6);
    newParam.setMembership(membership);

    return save(newParam);
  }
}