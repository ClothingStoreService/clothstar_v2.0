package org.store.clothstar.member.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.entity.MemberEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class MemberJpaRepositoryAdapter implements MemberRepository {
    MemberJpaRepository memberJpaRepository;

    MemberJpaRepositoryAdapter(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public List<Member> findAll() {
        List<MemberEntity> memberEntityList = memberJpaRepository.findAll();

        return memberEntityList.stream()
                .map(Member::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberJpaRepository.findById(memberId).map(Member::new);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        MemberEntity memberEntity = memberJpaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 아이디를 찾을 수 없습니다."));

        return Optional.of(new Member(memberEntity));
    }

    @Override
    public int update(Member member) {
        MemberEntity memberEntity = memberJpaRepository.findById(member.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + member.getMemberId()));

        memberEntity.updateMember(member, memberEntity);
        return 1;
    }

    @Override
    public int save(Member member) {
        MemberEntity memberEntity = Member.toMemberEntityByMember(member);
        memberJpaRepository.save(memberEntity);
        return 1;
    }
}
