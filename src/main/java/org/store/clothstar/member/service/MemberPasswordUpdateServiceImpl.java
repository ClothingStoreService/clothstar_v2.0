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
public class MemberPasswordUpdateServiceImpl implements MemberPasswordUpdateService {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public void updatePassword(Long memberId, String password) {
        MemberEntity memberEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));

        log.info("Update member password: {} -> {}", memberEntity.getPassword(), password);
        memberEntity.updatePassword(password);
    }
}
