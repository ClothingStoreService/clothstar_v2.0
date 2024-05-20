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
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.ModifyMemberRequest;
import org.store.clothstar.member.dto.response.MemberResponse;
import org.store.clothstar.member.service.MemberService;

import java.util.List;

@Tag(name = "Member", description = "회원 정보 관리에 대한 API 입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "전체 회원 조회", description = "전체 회원 리스트를 가져온다.")
    @GetMapping("/v1/members")
    public ResponseEntity<List<MemberResponse>> getAllMember() {
        List<MemberResponse> memberList = memberService.getAllMember();
        return ResponseEntity.ok(memberList);
    }

    @Operation(summary = "회원 상세정보 조회", description = "회원 한 명에 대한 상세 정보를 가져온다.")
    @GetMapping("/v1/members/{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable("id") Long memberId) {
        MemberResponse member = memberService.getMemberById(memberId);
        return ResponseEntity.ok(member);
    }

    @Operation(summary = "이메일 중복 체크", description = "이메일 중복체크를 한다.")
    @GetMapping("/v1/members/email/{email}")
    public ResponseEntity<MessageDTO> emailCheck(@PathVariable String email) {
        return ResponseEntity.ok(memberService.emailCheck(email));
    }

    @Operation(summary = "회원 상세정보 수정", description = "회원 정보를 수정한다.")
    @PutMapping("/v1/members/{id}")
    public ResponseEntity<MessageDTO> putModifyMember(@PathVariable("id") Long memberId,
                                                      @RequestBody ModifyMemberRequest modifyMemberRequest) {
        log.info("회원수정 요청 데이터 : memberId={}, {}", memberId, modifyMemberRequest.toString());
        return ResponseEntity.ok(memberService.modifyMember(memberId, modifyMemberRequest));
    }

    @Operation(summary = "회원가입", description = "회원가입시 회원 정보를 저장한다.")
    @PostMapping("/v1/members")
    public ResponseEntity<MessageDTO> signup(@Validated @RequestBody CreateMemberRequest createMemberDTO) {
        log.info("회원가입 요청 데이터 : {}", createMemberDTO.toString());
        return new ResponseEntity<>(memberService.signup(createMemberDTO), HttpStatus.CREATED);
    }
}