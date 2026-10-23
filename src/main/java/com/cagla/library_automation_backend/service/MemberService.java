package com.cagla.library_automation_backend.service;

import com.cagla.library_automation_backend.entity.Member;
import com.cagla.library_automation_backend.exception.BadRequestException;
import com.cagla.library_automation_backend.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member addMember(Member member) {
        if (member.getFullName() == null || member.getFullName().trim().isEmpty()) {
            throw new BadRequestException("Üye adı boş olamaz.");
        }

        return memberRepository.save(member);
    }
}