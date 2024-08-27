package com.sparta26.baemin.member.repository;

import com.sparta26.baemin.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
