package org.store.clothstar.member.repository;

import org.apache.ibatis.annotations.Mapper;
import org.store.clothstar.member.domain.Member;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMybatisRepository extends MemberRepository {

    List<Member> findAll();

    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmail(String email);

    int update(Member member);

    int save(Member member);
}