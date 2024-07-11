package org.store.clothstar.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.common.util.MessageDTOBuilder;
import org.store.clothstar.member.application.MemberServiceApplication;
import org.store.clothstar.member.dto.request.CertifyNumRequest;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.MemberLoginRequest;

@Tag(name = "Auth", description = "회원가입과 인증에 관한 API 입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final MemberServiceApplication memberServiceApplication;

    @Operation(summary = "회원가입", description = "회원가입시 회원 정보를 저장한다.")
    @PostMapping("/v1/members")
    public ResponseEntity<MessageDTO> signup(@Validated @RequestBody CreateMemberRequest createMemberDTO) {
        log.info("회원가입 요청 데이터 : {}", createMemberDTO.toString());
        memberServiceApplication.signup(createMemberDTO);

        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(
                HttpStatus.CREATED.value(),
                "회원가입이 정상적으로 되었습니다."
        );

        return new ResponseEntity<>(messageDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "회원 로그인", description = "아이디와 비밀번호를 입력후 로그인을 진행합니다.")
    @PostMapping("/v1/members/login")
    public void login(@Validated @RequestBody MemberLoginRequest memberLoginRequest) {
        // 실제 로그인 로직은 Spring Security에서 처리
    }

    @Operation(summary = "이메일로 인증번호 전송", description = "기입한 이메일로 인증번호를 전송합니다.")
    @PostMapping("/v1/members/auth")
    public void signupEmailAuthentication(@Validated @RequestBody CertifyNumRequest certifyNumRequest) {
        memberServiceApplication.signupCertifyNumEmailSend(certifyNumRequest.getEmail());
    }
}