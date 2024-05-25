package org.store.clothstar.member.repository;

import org.springframework.stereotype.Repository;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.entity.MemberEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemberMybatisRepositoryAdapter {
    private final MemberMybatisRepository memberMybatisRepository;

    public MemberMybatisRepositoryAdapter(MemberMybatisRepository memberMybatisRepository) {
        this.memberMybatisRepository = memberMybatisRepository;
    }

    public List<MemberEntity> findAll() {
        List<Member> memberList = memberMybatisRepository.findAll();

        return memberList.stream()
                .map(MemberEntity::new)
                .collect(Collectors.toList());
    }

    public Optional<MemberEntity> findById(Long memberId) {
        return memberMybatisRepository.findById(memberId)
                .map(MemberEntity::new);
    }

    public Optional<MemberEntity> findByEmail(String email) {
        return memberMybatisRepository.findByEmail(email)
                .map(MemberEntity::new);
    }

    public int update(Member member) {
        return memberMybatisRepository.update(member);
    }

    public int save(Member member) {
        return memberMybatisRepository.save(member);
    }
}