package org.store.clothstar.member.repository;

import org.springframework.stereotype.Repository;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.entity.MemberEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepositoryAdapter implements MemberRepository, NewMemberRepository {
    MemberJpaRepository memberJpaRepository;

    MemberJpaRepositoryAdapter(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public List<MemberEntity> findAll() {
        return memberJpaRepository.findAll();
    }

    @Override
    public Optional<MemberEntity> findById(Long memberId) {
        return memberJpaRepository.findById(memberId);
    }

    @Override
    public Optional<MemberEntity> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public int update(Member member) {
        return 0;
    }

    @Override
    public int save(Member member) {
        return 0;
    }

    @Override
    public void updateDeleteAt(Long memberId) {

    }
}
