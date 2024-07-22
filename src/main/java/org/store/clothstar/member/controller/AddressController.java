package org.store.clothstar.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.store.clothstar.common.dto.ErrorResponseDTO;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.common.util.MessageDTOBuilder;
import org.store.clothstar.member.application.AddressServiceApplication;
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.dto.response.AddressResponse;
import org.store.clothstar.member.dto.response.MemberResponse;

import java.util.List;

@Tag(name = "Member")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AddressController {
    private final AddressServiceApplication addressServiceApplication;

    @Operation(summary = "회원 배송지 전체 리스트 조회", description = "회원 한명에 대한 배송지를 전부 가져온다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 한명에 대한 배송지 전체 조회 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/v1/members/addresses/{id}")
    public ResponseEntity<List<AddressResponse>> getMemberAllAddressgetMemberAllAddress(@PathVariable("id") Long memberId) {
        List<AddressResponse> memberList = addressServiceApplication.getMemberAllAddress(memberId);
        return ResponseEntity.ok(memberList);
    }

    @Operation(summary = "회원 배송지 저장", description = "회원 한명에 대한 배송지를 저장한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "주소가 정상적으로 저장 되었습니다.",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/v1/members/addresses/{id}")
    public ResponseEntity<MessageDTO> addrSave(@Validated @RequestBody CreateAddressRequest createAddressRequest,
                                               @PathVariable("id") Long memberId) {
        log.info("회원 배송지 저장 요청 데이터 : {}", createAddressRequest.toString());

        addressServiceApplication.addrSave(memberId, createAddressRequest);

        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(
                HttpStatus.CREATED.value(),
                "주소가 정상적으로 저장 되었습니다."
        );

        return new ResponseEntity<>(messageDTO, HttpStatus.CREATED);
    }
}