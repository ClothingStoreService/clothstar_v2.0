package org.store.clothstar.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updatePassword(Long memberId, String password) {
        MemberEntity memberEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));

        String encodedPassword = passwordEncoder.encode(password);
        validCheck(memberEntity, encodedPassword);

        memberEntity.updatePassword(encodedPassword);
    }

    private void validCheck(MemberEntity memberEntity, String encodedPassword) {
        String originalPassword = memberEntity.getPassword();
        if (passwordEncoder.matches(originalPassword, encodedPassword)) {
            throw new IllegalArgumentException("이전 비밀번호와 같은 비밀번호 입니다.");
        }
    }
}
