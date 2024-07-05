package org.store.clothstar.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.store.clothstar.member.entity.MemberEntity;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    List<MemberEntity> findAll();

    Page<MemberEntity> findAllOffsetPaging(Pageable pageable);

    Slice<MemberEntity> findAllSlicePaging(Pageable pageable);

    Optional<MemberEntity> findById(Long memberId);

    Optional<MemberEntity> findByEmail(String email);

    MemberEntity save(MemberEntity memberEntity);
}