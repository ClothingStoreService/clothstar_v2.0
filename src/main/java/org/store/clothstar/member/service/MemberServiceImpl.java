package org.store.clothstar.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.store.clothstar.common.error.ErrorCode;
import org.store.clothstar.common.error.exception.SignupCertifyNumAuthFailedException;
import org.store.clothstar.common.mail.MailContentBuilder;
import org.store.clothstar.common.mail.MailSendDTO;
import org.store.clothstar.common.mail.MailService;
import org.store.clothstar.common.redis.RedisUtil;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.ModifyMemberRequest;
import org.store.clothstar.member.dto.response.MemberResponse;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.repository.MemberRepository;

import java.util.List;
import java.util.Random;
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
    private final RedisUtil redisUtil;

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

        //인증코드 확인
        boolean certifyStatus = verifyEmailCode(memberEntity.getEmail(), createMemberDTO.getCertifyNum());
        if (certifyStatus) {
            memberEntity = memberRepository.save(memberEntity);
        } else {
            throw new SignupCertifyNumAuthFailedException(ErrorCode.INVALID_AUTH_CERTIFY_NUM);
        }

        return memberEntity.getMemberId();
    }

    @Override
    public void signupCertifyNumEmailSend(String email) {
        sendEmailAuthentication(email);
        log.info("인증번호 전송 완료, email = {}", email);
    }

    private String sendEmailAuthentication(String toEmail) {
        String certifyNum = createdCode();
        String message = mailContentBuilder.build(certifyNum);
        MailSendDTO mailSendDTO = new MailSendDTO(toEmail, "clothstar 회원가입 인증 메일 입니다.", message);

        mailService.sendMail(mailSendDTO);

        //메일 전송에 성공하면 redis에 key = email, value = 인증번호를 생성한다.
        //지속시간은 10분
        createRedis(toEmail, certifyNum);

        return certifyNum;
    }

    public Boolean verifyEmailCode(String email, String certifyNum) {
        String codeFoundByEmail = redisUtil.getData(email);
        if (codeFoundByEmail == null) {
            return false;
        }

        return codeFoundByEmail.equals(certifyNum);
    }

    private void createRedis(String toEmail, String code) {
        if (redisUtil.existData(toEmail)) {
            redisUtil.deleteData(toEmail);
        }

        redisUtil.setDataExpire(toEmail, code);
    }

    private String createdCode() {
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}