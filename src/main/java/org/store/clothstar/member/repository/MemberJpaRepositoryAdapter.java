package org.store.clothstar.member.repository;

import org.store.clothstar.member.entity.Member;

import java.util.List;
import java.util.Optional;

public class MemberJpaRepositoryAdapter implements Repository {
    private MemberJpaRepository repository;

    public MemberJpaRepositoryAdapter(MemberJpaRepository repository) {
        repository = repository;
    }

    @Override
    public List<Member> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return repository.findById(memberId);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return null;
    }
//
//    @Override
//    public int update(Member member) {
//        return null;
//    }
//
//    @Override
//    public Object save(Object o) {
//        return null;
//    }
}
