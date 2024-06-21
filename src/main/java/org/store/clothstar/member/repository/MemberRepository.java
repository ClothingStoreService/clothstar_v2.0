package org.store.clothstar.member.repository;

import org.store.clothstar.member.entity.MemberEntity;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    List<MemberEntity> findAll();

    Optional<MemberEntity> findById(Long memberId);

    Optional<MemberEntity> findByEmail(String email);

    MemberEntity save(MemberEntity memberEntity);
}