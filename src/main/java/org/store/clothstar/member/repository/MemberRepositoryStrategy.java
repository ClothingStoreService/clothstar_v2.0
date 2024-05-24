package org.store.clothstar.member.repository;

import org.springframework.stereotype.Repository;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.entity.MemberEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepositoryStrategy {
    private MemberRepository memberRepository;

    public void setMemberRepositoryStrategy(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberEntity> findAll() {
        return this.memberRepository.findAll();
    }

    public Optional<MemberEntity> findById(Long memberId) {
        return this.memberRepository.findById(memberId);
    }

    public Optional<MemberEntity> findByEmail(String email) {
        return this.memberRepository.findByEmail(email);
    }

    public int update(Member member) {
        return this.memberRepository.update(member);
    }

    public int save(Member member) {
        return 0;
    }
}
