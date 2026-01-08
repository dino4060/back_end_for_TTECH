package com.dino.back_end_for_TTECH.features.promotion.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Type;

import com.dino.back_end_for_TTECH.features.promotion.domain.model.PromoType;
import com.dino.back_end_for_TTECH.shared.application.utils.AppGen;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "vouchers")
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "UPDATE vouchers SET is_deleted_child = true WHERE campaign_id=?")
@SQLRestriction("is_deleted_child = false")
@DiscriminatorValue("VOUCHER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "promotion_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class Coupon extends Campaign {

  String couponType;

  String couponCode;

  Boolean isFixed;

  Integer discountValue;

  Integer minSpend;

  Integer maxDiscount;

  Integer validityDays;

  Integer totalLimit;

  Integer limitPerCustomer;

  Integer usedCount = 0;

  @Type(JsonType.class)
  @Column(columnDefinition = "jsonb")
  Map<Long, Integer> countPerCustomer = new HashMap<>();

  Boolean isApplyAll;

  @OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  List<CouponUnit> units = new ArrayList<>();

  public Coupon(Long id) {
    this.id = id;
  }

  Boolean isDeletedChild = false;

  /**
   * Description
   * - Sync status based on dates
   * - Generate coupon code if needed
   * - Clear max discount if fixed discount
   */
  public void processIntegrity() {
    this.syncStatus();
    this.processCouponType();
    this.processCouponCode();
    this.processMaxDiscount();
  }

  /**
   * Description:
   * - Set coupon type same promotion type
   */
  private void processCouponType() {
    this.setCouponType(this.getPromotionType());
  }

  /**
   * Description:
   * - Clear code if not CODE_VOUCHER type
   * - Generate random code if CODE_VOUCHER and code is null
   */
  private void processCouponCode() {
    var isCouponCode = PromoType.COUPON_CODE.toString().equals(this.getPromotionType());

    if (!isCouponCode) {
      this.setCouponCode(null);
    } else if (this.getCouponCode() == null) {
      this.setCouponCode(AppGen.randomCode(8));
    }
  }

  /**
   * Description:
   * - Clear max discount if using fixed discount
   */
  private void processMaxDiscount() {
    var isFixed = Boolean.TRUE.equals(this.getIsFixed());
    var isCouponCode = PromoType.COUPON_CODE.toString().equals(this.getPromotionType());

    if (isFixed) {
      this.setMaxDiscount(null);
    } else if (isCouponCode && this.getMaxDiscount() != null) {
      this.setMaxDiscount(1);
    }
  }
}