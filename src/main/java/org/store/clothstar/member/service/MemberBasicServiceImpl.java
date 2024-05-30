package org.store.clothstar.member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.dto.request.ModifyMemberRequest;
import org.store.clothstar.member.dto.response.MemberResponse;
import org.store.clothstar.member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 기존 Mybatis기능과 Jpa기능이 같이 있는 서비스 로직을 구현한 클래스
 * - memberJpaRepositoryAdapter클래스가 Jpa 기능으로 구현한 클래스
 * - memberMybatisRepository가 Mybatis로 구현한 클래스
 */
@Service
@Slf4j
public class MemberBasicServiceImpl implements MemberBasicService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberBasicServiceImpl(
            @Qualifier("memberJpaRepositoryAdapter") MemberRepository memberRepository
            //@Qualifier("memberMybatisRepository") MemberRepository memberRepository
            , PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public MemberResponse findById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberResponse::new)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));
    }

    @Override
    public boolean findByEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    @Override
    public void update(Long memberId, ModifyMemberRequest modifyMemberRequest) {
        Member member = modifyMemberRequest.toMember(memberId);
        memberRepository.update(member);
    }


}
