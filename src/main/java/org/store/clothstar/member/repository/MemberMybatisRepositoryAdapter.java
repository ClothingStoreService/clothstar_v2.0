package org.store.clothstar.member.repository;

import org.springframework.stereotype.Repository;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.entity.MemberEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemberMybatisRepositoryAdapter implements MemberRepository {
    private final MemberMybatisRepository memberMybatisRepository;

    public MemberMybatisRepositoryAdapter(MemberMybatisRepository memberMybatisRepository) {
        this.memberMybatisRepository = memberMybatisRepository;
    }

    @Override
    public List<MemberEntity> findAll() {
        List<Member> memberList = memberMybatisRepository.findAll();

        return memberList.stream()
                .map(MemberEntity::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MemberEntity> findById(Long memberId) {
        return memberMybatisRepository.findById(memberId)
                .map(MemberEntity::new);
    }

    @Override
    public Optional<MemberEntity> findByEmail(String email) {
        return memberMybatisRepository.findByEmail(email)
                .map(MemberEntity::new);
    }

    @Override
    public int update(Member member) {
        return memberMybatisRepository.update(member);
    }

    @Override
    public int save(Member member) {
        return memberMybatisRepository.save(member);
    }
}