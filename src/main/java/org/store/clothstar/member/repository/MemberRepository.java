package org.store.clothstar.member.repository;

import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.entity.MemberEntity;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    List<MemberEntity> findAll();

    Optional<MemberEntity> findById(Long memberId);

    Optional<MemberEntity> findByEmail(String email);

    int update(Member member);

    int save(Member member);
}