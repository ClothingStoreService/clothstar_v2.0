package org.store.clothstar.member.repository;

import org.store.clothstar.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    List<Member> findAll();

    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);

    void update(Member member);
}