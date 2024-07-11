package org.store.clothstar.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.store.clothstar.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Page<Member> findAllOffsetPaging(Pageable pageable);

    Slice<Member> findAllSlicePaging(Pageable pageable);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);

    Member save(Member member);
}