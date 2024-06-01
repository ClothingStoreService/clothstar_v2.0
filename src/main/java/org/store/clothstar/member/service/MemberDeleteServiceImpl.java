package org.store.clothstar.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.repository.MemberJpaRepository;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberDeleteServiceImpl implements MemberDeleteService {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public void updateDeleteAt(Long memberId) {
        MemberEntity memberEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));

        memberEntity.updateDeletedAt();
    }
}
