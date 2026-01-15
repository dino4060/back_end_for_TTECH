package com.dino.back_end_for_TTECH.features.membership.domain.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dino.back_end_for_TTECH.features.membership.domain.Member;
import com.dino.back_end_for_TTECH.features.membership.domain.Membership;
import com.dino.back_end_for_TTECH.features.profile.domain.User;
import com.dino.back_end_for_TTECH.shared.domain.BaseRepository;

@Repository
public interface MemberRepository extends BaseRepository<Member, Long> {

  boolean existsByMembership(Membership membership);

  List<Member> findByCustomerIn(List<User> customers);

  @EntityGraph(attributePaths = { "membership" })
  Optional<Member> findByCustomer(User customer);

  @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o " +
      "WHERE o.buyer = :customer " +
      "AND o.createdAt >= :since")
  int calcPointsByCustomerFrom(User customer, Instant since);

  // @Query("""
  // SELECT COALESCE(SUM(o.total), 0)
  // FROM Order o
  // WHERE o.buyer.id = :customerId
  // AND o.createdAt >= :fromDate
  // AND o.status = 'COMPLETED'
  // """)
  // Long calcPointsByCustomerFromLegacy(
  // @Param("customerId") Long customerId,
  // @Param("fromDate") LocalDateTime fromDate);
}
