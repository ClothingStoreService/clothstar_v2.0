package org.store.clothstar.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.store.clothstar.common.error.ErrorCode;
import org.store.clothstar.common.error.exception.DuplicatedEmailException;
import org.store.clothstar.common.error.exception.NotFoundMemberException;
import org.store.clothstar.common.error.exception.SignupCertifyNumAuthFailedException;
import org.store.clothstar.common.mail.MailContentBuilder;
import org.store.clothstar.common.mail.MailSendDTO;
import org.store.clothstar.common.mail.MailService;
import org.store.clothstar.common.redis.RedisUtil;
import org.store.clothstar.member.domain.Account;
import org.store.clothstar.member.domain.Authorization;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.MemberRole;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.ModifyNameRequest;
import org.store.clothstar.member.dto.response.MemberResponse;
import org.store.clothstar.member.repository.AccountRepository;
import org.store.clothstar.member.repository.AuthorizationRepository;
import org.store.clothstar.member.repository.MemberRepository;

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
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final AuthorizationRepository authorizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private final RedisUtil redisUtil;

    @Override
    public Page<MemberResponse> getAllMemberOffsetPaging(Pageable pageable) {
        return memberRepository.findAllOffsetPaging(pageable)
                .map(MemberResponse::new);
    }

    @Override
    public Slice<MemberResponse> getAllMemberSlicePaging(Pageable pageable) {
        return memberRepository.findAllSlicePaging(pageable)
                .map(MemberResponse::new);
    }

    @Override
    public MemberResponse getMemberById(Long memberId) {
        log.info("회원 상세 조회 memberId = {}", memberId);
        return memberRepository.findById(memberId)
                .map(MemberResponse::new)
                .orElseThrow(() -> new NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER));
    }

    @Override
    public void getMemberByEmail(String email) {
        if (accountRepository.findByEmail(email).isPresent()) {
            throw new DuplicatedEmailException(ErrorCode.DUPLICATED_EMAIL);
        }
    }

    @Override
    public void modifyName(Long memberId, ModifyNameRequest modifyNameRequest) {
        log.info("회원 이름 수정 memberId = {}, name = {}", memberId, modifyNameRequest.getName());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER));

        member.updateName(modifyNameRequest);
    }

    @Override
    public void updateDeleteAt(Long memberId) {
        log.info("회원 삭제 memberId = {}", memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER));

        member.updateDeletedAt();
    }

    @Override
    public void updatePassword(Long memberId, String password) {
        log.info("회원 비밀번호 변경 memberId = {}", memberId);
        Account account = accountRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER));

        String encodedPassword = passwordEncoder.encode(password);
        validCheck(account, encodedPassword);

        account.updatePassword(encodedPassword);
    }

    private void validCheck(Account account, String encodedPassword) {
        String originalPassword = account.getPassword();
        if (passwordEncoder.matches(originalPassword, encodedPassword)) {
            throw new IllegalArgumentException("이전 비밀번호와 같은 비밀번호 입니다.");
        }
    }

    @Override
    public Long signUp(CreateMemberRequest createMemberDTO) {
        accountRepository.findByEmail(createMemberDTO.getEmail()).ifPresent(m -> {
            throw new DuplicatedEmailException(ErrorCode.DUPLICATED_EMAIL);
        });

        String encodedPassword = passwordEncoder.encode(createMemberDTO.getPassword());
        Account account = createMemberDTO.toAccount(encodedPassword);

        //인증코드 확인
        boolean certifyStatus = verifyEmailCertifyNum(createMemberDTO.getEmail(), createMemberDTO.getCertifyNum());
        if (certifyStatus) {
            account = accountRepository.save(account);
            Member member = createMemberDTO.toMember(account.getAccountId());
            memberRepository.save(member);

            Authorization authorization = new Authorization(account, MemberRole.USER);
            authorizationRepository.save(authorization);
        } else {
            throw new SignupCertifyNumAuthFailedException(ErrorCode.INVALID_AUTH_CERTIFY_NUM);
        }

        return account.getAccountId();
    }

    @Override
    public void signupCertifyNumEmailSend(String email) {
        sendEmailAuthentication(email);
        log.info("인증번호 전송 완료, email = {}", email);
    }

    @Override
    public Member getMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private String sendEmailAuthentication(String toEmail) {
        String certifyNum = redisUtil.createdCertifyNum();
        String message = mailContentBuilder.build(certifyNum);
        MailSendDTO mailSendDTO = new MailSendDTO(toEmail, "clothstar 회원가입 인증 메일 입니다.", message);

        mailService.sendMail(mailSendDTO);

        //메일 전송에 성공하면 redis에 key = email, value = 인증번호를 생성한다.
        //지속시간은 10분
        redisUtil.createRedisData(toEmail, certifyNum);

        return certifyNum;
    }

    public Boolean verifyEmailCertifyNum(String email, String certifyNum) {
        String certifyNumFoundByRedis = redisUtil.getData(email);
        if (certifyNumFoundByRedis == null) {
            return false;
        }

        return certifyNumFoundByRedis.equals(certifyNum);
    }
}