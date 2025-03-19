package com.szs.repository;

import com.szs.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByNameAndRegNo(String name, String regNo);

    boolean existsByUserId(String userId);

    Optional<Member> findByUserId(String userId);
}
