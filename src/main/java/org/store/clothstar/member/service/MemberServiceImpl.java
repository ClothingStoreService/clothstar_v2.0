package org.store.clothstar.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.store.clothstar.common.mail.MailContentBuilder;
import org.store.clothstar.common.mail.MailSendDTO;
import org.store.clothstar.common.mail.MailService;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.ModifyMemberRequest;
import org.store.clothstar.member.dto.response.MemberResponse;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 기존 Mybatis기능과 Jpa기능이 같이 있는 서비스 로직을 구현한 클래스
 * - memberJpaRepositoryAdapter클래스가 Jpa 기능으로 구현한 클래스
 * - memberMybatisRepository가 Mybatis로 구현한 클래스
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    @Override
    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public MemberResponse getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberResponse::new)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));
    }

    @Override
    public boolean getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    @Override
    public void modifyMember(Long memberId, ModifyMemberRequest modifyMemberRequest) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));

        memberEntity.updateMember(modifyMemberRequest, memberEntity);
    }

    @Override
    public void updateDeleteAt(Long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));

        memberEntity.updateDeletedAt();
    }

    @Override
    public void updatePassword(Long memberId, String password) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
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

    @Override
    public Long signUp(CreateMemberRequest createMemberDTO) {
        memberRepository.findByEmail(createMemberDTO.getEmail()).ifPresent(m -> {
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        });

        String encodedPassword = passwordEncoder.encode(createMemberDTO.getPassword());
        MemberEntity memberEntity = createMemberDTO.toMemberEntity(encodedPassword);
        memberEntity.updateEnabled(false);
        memberEntity = memberRepository.save(memberEntity);

        sendEmailAuthentication(memberEntity.getMemberId(), memberEntity.getEmail());

        return memberEntity.getMemberId();
    }

    @Override
    public void signupEmailAuthentication(Long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("not found by memberId: " + memberId));

        memberEntity.updateEnabled(true);
        log.info("이메일 인증 완료, email = {}", memberEntity.getEmail());
    }

    private void sendEmailAuthentication(Long memberId, String email) {
        String link = "http://localhost:8080/v1/members/auth/" + memberId;
        String message = mailContentBuilder.build(link);
        MailSendDTO mailSendDTO = new MailSendDTO(email, "clothstar 회원가입 인증 메일 입니다.", message);

        mailService.sendMail(mailSendDTO);
    }
}