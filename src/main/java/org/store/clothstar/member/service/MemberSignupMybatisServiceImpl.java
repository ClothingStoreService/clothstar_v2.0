package org.store.clothstar.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.repository.MemberMybatisRepository;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberSignupMybatisServiceImpl implements MemberSignupService {
    private final MemberMybatisRepository memberMybatisRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long signUp(CreateMemberRequest createMemberDTO) {
        String encryptedPassword = passwordEncoder.encode(createMemberDTO.getPassword());
        Member member = createMemberDTO.toMember(encryptedPassword);

        int result = memberMybatisRepository.save(member);
        if (result == 0) {
            throw new IllegalArgumentException("회원 가입이 되지 않았습니다.");
        }

        return member.getMemberId();
    }
}
