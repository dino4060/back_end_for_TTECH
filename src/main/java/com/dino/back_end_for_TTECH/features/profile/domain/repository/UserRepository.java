package com.dino.back_end_for_TTECH.features.profile.domain.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dino.back_end_for_TTECH.features.profile.domain.User;

public interface UserRepository extends
    JpaRepository<User, Long>,
    JpaSpecificationExecutor<User> {

  Optional<User> findByEmail(String email);

  Optional<User> findByPhone(String phone);

  Optional<User> findByEmailAndIdNot(String email, Long excludedId);

  Optional<User> findByPhoneAndIdNot(String phone, Long excludedId);

  // value is NULL, database exists NULL, return EMPTY: O (safe)
  Optional<User> findByUsername(String username);

  @Query("""
      SELECT COALESCE(SUM(o.totalPayment), 0)
      FROM Order o
      WHERE o.buyer.id = :customerId
      AND o.createdAt >= :fromDate
      """)
  Integer sumPaymentByCustomerWithin6Months(
      @Param("customerId") Long customerId,
      @Param("fromDate") LocalDateTime fromDate);

  @Query("SELECT SUM(o.totalAmount) FROM Order o " +
      "WHERE o.buyer = :customer " +
      // "AND o.status = 'COMPLETED' " + // Chỉ tính các đơn đã hoàn thành
      "AND o.createdAt >= :since")
  Integer sumPaymentByCustomerSince(User customer, LocalDateTime since);
}
