package org.store.clothstar.member.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.store.clothstar.member.domain.Member;

@Mapper
public interface MemberMybatisRepository {

	List<Member> findAll();

	Optional<Member> findById(Long memberId);

	Optional<Member> findByEmail(String email);

	int update(Member member);

	int save(Member member);
}