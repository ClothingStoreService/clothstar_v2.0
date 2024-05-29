package org.store.clothstar.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.common.util.MessageDTOBuilder;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.ModifyMemberRequest;
import org.store.clothstar.member.dto.request.ModifyPasswordRequest;
import org.store.clothstar.member.dto.response.MemberResponse;
import org.store.clothstar.member.service.MemberServiceApplication;

import java.util.List;

@Tag(name = "Member", description = "회원 정보 관리에 대한 API 입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberServiceApplication memberServiceApplication;

    @Operation(summary = "전체 회원 조회", description = "전체 회원 리스트를 가져온다.")
    @GetMapping("/v1/members")
    public ResponseEntity<List<MemberResponse>> getAllMember() {
        List<MemberResponse> memberList = memberServiceApplication.getAllMember();
        return ResponseEntity.ok(memberList);
    }

    @Operation(summary = "회원 상세정보 조회", description = "회원 한 명에 대한 상세 정보를 가져온다.")
    @GetMapping("/v1/members/{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable("id") Long memberId) {
        MemberResponse member = memberServiceApplication.getMemberById(memberId);
        return ResponseEntity.ok(member);
    }

    @Operation(summary = "이메일 중복 체크", description = "이메일 중복체크를 한다.")
    @GetMapping("/v1/members/email/{email}")
    public ResponseEntity<MessageDTO> emailCheck(@PathVariable String email) {

        boolean emailExists = memberServiceApplication.emailCheck(email);

        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(
                HttpStatus.OK.value(),
                (emailExists ? "이미 사용중인 이메일 입니다." : "사용 가능한 이메일 입니다."),
                emailExists
        );

        return ResponseEntity.ok(messageDTO);
    }

    @Operation(summary = "회원 상세정보 수정", description = "회원 정보를 수정한다.")
    @PutMapping("/v1/members/{id}")
    public ResponseEntity<MessageDTO> modifyMember(@PathVariable("id") Long memberId,
                                                   @RequestBody ModifyMemberRequest modifyMemberRequest) {
        log.info("회원수정 요청 데이터 : memberId={}, {}", memberId, modifyMemberRequest.toString());

        memberServiceApplication.modifyMember(memberId, modifyMemberRequest);

        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(
                HttpStatus.OK.value(),
                "memberId : " + memberId + " 가 정상적으로 수정 되었습니다.",
                true
        );

        return ResponseEntity.ok(messageDTO);
    }

    @Operation(summary = "회원 비밀번호 수정", description = "회원 비밀번호를 수정한다.")
    @PatchMapping("/v1/members/{id}")
    public ResponseEntity<MessageDTO> modifyPassword(@PathVariable("id") Long memberId,
                                                     @Validated @RequestBody ModifyPasswordRequest modifyPasswordRequest) {
        log.info("회원 비밀번호 요청 데이터 : memberId={}, password={}", memberId, modifyPasswordRequest);

        memberServiceApplication.modifyPassword(memberId, modifyPasswordRequest.getPassword());

        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(
                HttpStatus.OK.value(),
                "memberId : " + memberId + " 의 비밀번호가 변경 되었습니다.",
                true
        );

        return ResponseEntity.ok(messageDTO);
    }

    @Operation(summary = "회원 삭제", description = "회원 삭제시간을 현재시간으로 업데이트 합니다.")
    @DeleteMapping("/v1/members/{id}")
    public ResponseEntity<MessageDTO> deleteMember(@PathVariable("id") Long memberId) {
        log.info("회원삭제 요청 데이터 : memberId={}", memberId);

        memberServiceApplication.updateDeleteAt(memberId);

        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(
                HttpStatus.OK.value(),
                "memberId : " + memberId + " 가 정상적으로 삭제 되었습니다.",
                true
        );

        return ResponseEntity.ok(messageDTO);
    }

    @Operation(summary = "회원가입", description = "회원가입시 회원 정보를 저장한다.")
    @PostMapping("/v1/members")
    public ResponseEntity<MessageDTO> signup(@Validated @RequestBody CreateMemberRequest createMemberDTO) {
        log.info("회원가입 요청 데이터 : {}", createMemberDTO.toString());

        Long memberId = memberServiceApplication.signup(createMemberDTO);

        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(
                memberId,
                HttpStatus.OK.value(),
                "memberId : " + memberId + " 가 정상적으로 회원가입 되었습니다."
        );

        return new ResponseEntity<>(messageDTO, HttpStatus.CREATED);

    }
}