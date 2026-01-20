package com.dino.back_end_for_TTECH.features.membership;

import com.dino.back_end_for_TTECH.features.membership.domain.Member;
import com.dino.back_end_for_TTECH.features.membership.domain.Membership;
import com.dino.back_end_for_TTECH.features.membership.domain.model.MemberStatus;
import com.dino.back_end_for_TTECH.features.profile.domain.User;

import lombok.experimental.FieldDefaults;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Membership Domain Tests")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
class MembershipTest {

  Membership membership;
  User customer;

  @BeforeEach
  void setUp() {
    membership = new Membership();
    membership.setId(1L);
    membership.setMembershipName("Gold Member");
    membership.setMembershipCode("GOLD");
    membership.setMinPoint(1000);

    customer = new User();
    customer.setId(99L);
  }

  @Test
  @DisplayName("Nên tạo mới một Member với đầy đủ thông tin khi Enroll")
  void shouldEnrollMemberSuccessfully() {
    Member result = membership.enrollMember(customer);

    assertThat(result).isNotNull();
    assertThat(result.getMembership()).isEqualTo(membership);
    assertThat(result.getCustomer()).isEqualTo(customer);
    assertThat(result.getPoints()).isEqualTo(0);
    assertThat(result.getRankedAt()).isNotNull();
    assertThat(result.hasStatus(MemberStatus.UPGRADE)).isTrue();

    assertThat(membership.getIsAlive()).isTrue();
    assertThat(membership.getValidityMonths()).isEqualTo(6);
  }

  @Test
  @DisplayName("Kiểm tra tính đóng gói và Setter/Getter của Membership")
  void testMembershipProperties() {
    Membership newMembership = new Membership();
    newMembership.setMembershipName("Platinum");

    assertThat(newMembership.getMembershipName()).isEqualTo("Platinum");
    assertThat(newMembership.getBenefits()).isEmpty();
  }
}