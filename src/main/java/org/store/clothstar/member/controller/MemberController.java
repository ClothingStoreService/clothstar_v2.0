package org.store.clothstar.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.store.clothstar.common.dto.ErrorResponseDTO;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.common.util.MessageDTOBuilder;
import org.store.clothstar.member.application.MemberServiceApplication;
import org.store.clothstar.member.dto.request.ModifyNameRequest;
import org.store.clothstar.member.dto.request.ModifyPasswordRequest;
import org.store.clothstar.member.dto.response.MemberResponse;

@Tag(name = "Member", description = "회원 정보 관리에 대한 API 입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberServiceApplication memberServiceApplication;

    @Operation(summary = "전체 회원 조회 offset 페이징", description = "전체 회원 리스트를 offset 페이징 형식으로 가져온다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 리스트 offset 페이징 조회 성공",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/v1/members")
    public ResponseEntity<Page<MemberResponse>> getAllMemberOffsetPaging(
            @PageableDefault(size = 18) Pageable pageable) {
        Page<MemberResponse> memberPages = memberServiceApplication.getAllMemberOffsetPaging(pageable);
        return ResponseEntity.ok(memberPages);
    }

    @Operation(summary = "전체 회원 조회 slice 페이징", description = "전체 회원 리스트를 slice 페이징 형식으로 가져온다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 리스트 Slice 페이징 조회 성공",
                    content = @Content(schema = @Schema(implementation = Slice.class)))
    })
    @GetMapping("/v2/members")
    public ResponseEntity<Slice<MemberResponse>> getAllMemberSlicePaging(
            @PageableDefault(size = 18) Pageable pageable) {
        Slice<MemberResponse> memberPages = memberServiceApplication.getAllMemberSlicePaging(pageable);
        return ResponseEntity.ok(memberPages);
    }

    @Operation(summary = "회원 상세정보 조회", description = "회원 한 명에 대한 상세 정보를 가져온다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 상세정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/v1/members/{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable("id") Long memberId) {
        MemberResponse member = memberServiceApplication.getMemberById(memberId);
        return ResponseEntity.ok(member);
    }

    @Operation(summary = "이메일 중복 체크", description = "이메일 중복체크를 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 이메일 입니다.",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "이미 사용중인 이메일 입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/v1/members/email/{email}")
    public ResponseEntity<MessageDTO> emailDuplicationCheck(@PathVariable String email) {
        memberServiceApplication.emailCheck(email);

        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(
                HttpStatus.OK.value(),
                "사용 가능한 이메일 입니다."
        );

        return ResponseEntity.ok(messageDTO);
    }

    @Operation(summary = "회원 이름 수정", description = "회원 이름을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보가 수정 되었습니다.",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
    })
    @PatchMapping("/v1/members/name/{id}")
    public ResponseEntity<MessageDTO> modifyMember(@PathVariable("id") Long memberId,
                                                   @RequestBody ModifyNameRequest modifyNameRequest) {
        log.info("회원 이름수정 요청 데이터 : memberId={}, {}", memberId, modifyNameRequest.toString());

        memberServiceApplication.modifyName(memberId, modifyNameRequest);

        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(
                HttpStatus.OK.value(),
                "회원 이름이 수정 되었습니다."
        );

        return ResponseEntity.ok(messageDTO);
    }

    @Operation(summary = "회원 비밀번호 수정", description = "회원 비밀번호를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 비밀번호가 수정 되었습니다.",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
    })
    @PatchMapping("/v1/members/{id}")
    public ResponseEntity<MessageDTO> modifyPassword(@PathVariable("id") Long memberId,
                                                     @Validated @RequestBody ModifyPasswordRequest modifyPasswordRequest) {
        log.info("회원 비밀번호 요청 데이터 : memberId={}, password={}", memberId, modifyPasswordRequest);

        memberServiceApplication.modifyPassword(memberId, modifyPasswordRequest.getPassword());

        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(
                HttpStatus.OK.value(),
                "회원 비밀번호가 수정 되었습니다."
        );

        return ResponseEntity.ok(messageDTO);
    }

    @Operation(summary = "회원 삭제", description = "회원 삭제시간을 현재시간으로 업데이트 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원이 정상적으로 삭제 되었습니다.",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
    })
    @DeleteMapping("/v1/members/{id}")
    public ResponseEntity<MessageDTO> deleteMember(@PathVariable("id") Long memberId) {
        log.info("회원삭제 요청 데이터 : memberId={}", memberId);

        memberServiceApplication.updateDeleteAt(memberId);

        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(
                HttpStatus.OK.value(),
                "회원이 정상적으로 삭제 되었습니다."
        );

        return ResponseEntity.ok(messageDTO);
    }
}