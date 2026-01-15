package com.dino.back_end_for_TTECH.features.membership.domain;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import com.dino.back_end_for_TTECH.features.membership.domain.model.ApplyResult;
import com.dino.back_end_for_TTECH.features.membership.domain.model.BenefitUnit;
import com.dino.back_end_for_TTECH.shared.domain.model.BaseEntity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "member_benefits")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Benefit extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_benefits_seq")
  @SequenceGenerator(name = "member_benefits_seq", allocationSize = 6)
  @Column(name = "benefit_id")
  Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "membership_id")
  Membership membership;

  String benefitType;

  String benefitName;

  Integer benefitValue;

  String benefitUnit;

  Integer minSpend;

  Integer limitPerCustomer;

  @Type(JsonType.class)
  @Column(columnDefinition = "jsonb")
  Map<Long, Integer> countPerCustomer = new HashMap<>();

  Boolean isAlive = true;

  public void markAsUsed(Long customerId) {
    // Increment customer usage
    if (customerId != null) {
      Integer currentUsage = this.getCountPerCustomer().getOrDefault(customerId, 0);
      this.countPerCustomer.put(customerId, currentUsage + 1);
    }
  }

  public ApplyResult canApply(Long customerId, Integer spendAmount) {
    // 1. Kiểm tra số lần sử dụng cho mỗi khách hàng
    if (customerId != null && this.limitPerCustomer != null) {
      Integer usage = this.countPerCustomer.getOrDefault(customerId, 0);
      if (usage >= this.limitPerCustomer) {
        return ApplyResult.fail("Bạn đã hết lượt sử dụng");
      }
    }

    // 2. Kiểm tra chi tiêu tối thiểu
    if (this.minSpend != null && spendAmount < this.minSpend) {
      return ApplyResult.fail(String.format("Cần chi tiêu tối thiểu %,d,000 VND", this.minSpend));
    }

    // 3. Tính toán benefit value
    Integer benefitValue = 0;
    if (BenefitUnit.PERCENT.toString().equals(this.benefitUnit)) {
      benefitValue = (spendAmount * this.benefitValue) / 100;
    } else if (BenefitUnit.FIXED.toString().equals(this.benefitUnit)) {
      benefitValue = this.benefitValue;
    } else if (BenefitUnit.MONTHS.toString().equals(this.benefitUnit)) {
      benefitValue = this.benefitValue;
    }

    return ApplyResult.success(
        this.getId(),
        this.getBenefitType(),
        this.getBenefitName(),
        benefitValue);
  }
}
