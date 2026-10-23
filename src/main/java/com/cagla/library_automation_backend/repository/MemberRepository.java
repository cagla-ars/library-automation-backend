package com.cagla.library_automation_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cagla.library_automation_backend.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}