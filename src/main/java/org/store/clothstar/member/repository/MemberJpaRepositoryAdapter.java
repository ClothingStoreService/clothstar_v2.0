package org.store.clothstar.member.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.entity.MemberEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemberJpaRepositoryAdapter implements MemberRepository, NewMemberRepository {
    MemberJpaRepository memberJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

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
        return Optional.empty();
    }

    @Override
    public int update(Member member) {
        MemberEntity memberEntity = memberJpaRepository.findById(member.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + member.getMemberId()));

        memberEntity.updateMember(member);
        return 1;
    }

    @Override
    public int save(Member member) {
        return 0;
    }

    @Override
    public void updateDeleteAt(MemberEntity memberEntity) {
        memberEntity.updateDeletedAt(LocalDateTime.now());
    }
}
