package com.dino.back_end_for_TTECH.features.profile.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.jpa.domain.Specification;

import com.dino.back_end_for_TTECH.features.membership.application.model.MemberData;
import com.dino.back_end_for_TTECH.features.membership.domain.Member;
import com.dino.back_end_for_TTECH.features.profile.application.model.CustomerData;
import com.dino.back_end_for_TTECH.features.profile.application.model.UserBody;
import com.dino.back_end_for_TTECH.features.profile.application.model.UserData;
import com.dino.back_end_for_TTECH.features.profile.application.model.UserQuery;
import com.dino.back_end_for_TTECH.features.profile.domain.User;
import com.dino.back_end_for_TTECH.features.profile.domain.specification.UserSpec;
import com.dino.back_end_for_TTECH.shared.application.mapper.PageMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends PageMapper {

  default Specification<User> toQueryable(UserQuery query) {
    return UserSpec.fromQuery(query);
  }

  void toModel(UserBody body, @MappingTarget User model);

  UserData toData(User user);

  CustomerData toCustomerData(User user, MemberData member);

  MemberData toMemberData(Member user);
}
