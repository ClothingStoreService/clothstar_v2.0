package org.store.clothstar.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.store.clothstar.common.dto.SaveResponseDTO;
import org.store.clothstar.member.application.MemberServiceApplication;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.MemberLoginRequest;

@Tag(name = "Auth", description = "회원가입과 인증에 관한 API 입니다.")
@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final MemberServiceApplication memberServiceApplication;

    @Operation(summary = "회원가입", description = "회원가입시 회원 정보를 저장한다.")
    @ResponseBody
    @PostMapping("/v1/members")
    public ResponseEntity<SaveResponseDTO> signup(@Validated @RequestBody CreateMemberRequest createMemberDTO) {
        log.info("회원가입 요청 데이터 : {}", createMemberDTO.toString());

        Long memberId = memberServiceApplication.signup(createMemberDTO);

        SaveResponseDTO saveResponseDTO = SaveResponseDTO.builder()
                .id(memberId)
                .statusCode(HttpStatus.OK.value())
                .message(createMemberDTO.getEmail() + " 아이디로 회원가입이 완료 되었습니다.")
                .build();

        return new ResponseEntity<>(saveResponseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "회원 로그인", description = "아이디와 비밀번호를 입력후 로그인을 진행합니다.")
    @PostMapping("/v1/members/login")
    public void login(@RequestBody MemberLoginRequest memberLoginRequest) {
        // 실제 로그인 로직은 Spring Security에서 처리
    }

    @Operation(summary = "이메일로 인증번호 전송", description = "기입한 이메일로 인증번호를 전송합니다.")
    @ResponseBody
    @GetMapping("/v1/members/auth/{email}")
    public void signupEmailAuthentication(@PathVariable("email") String email) {
        memberServiceApplication.signupCertifyNumEmailSend(email);
    }
}