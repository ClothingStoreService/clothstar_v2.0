package org.store.clothstar.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.repository.MemberJpaRepository;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberSignupJpaServiceImpl implements MemberSignupService {
    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long signUp(CreateMemberRequest createMemberDTO) {
        memberJpaRepository.findByEmail(createMemberDTO.getEmail()).ifPresent(m -> {
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        });

        String encodedPassword = passwordEncoder.encode(createMemberDTO.getPassword());
        MemberEntity memberEntity = createMemberDTO.toMemberEntity(encodedPassword);
        memberEntity = memberJpaRepository.save(memberEntity);

        return memberEntity.getMemberId();
    }
}
