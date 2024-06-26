package org.store.clothstar.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.store.clothstar.common.dto.SaveResponseDTO;
import org.store.clothstar.member.application.AddressServiceApplication;
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.dto.response.AddressResponse;

import java.util.List;

@Tag(name = "Member")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AddressController {
    private final AddressServiceApplication addressServiceApplication;

    @Operation(summary = "회원 배송지 전체 리스트 조회", description = "회원 한 명에 대한 배송지를 전부 가져온다.")
    @GetMapping("/v1/members/addresses/{id}")
    public ResponseEntity<List<AddressResponse>> getMemberAllAddress(@PathVariable("id") Long memberId) {
        List<AddressResponse> memberList = addressServiceApplication.getMemberAllAddress(memberId);
        return ResponseEntity.ok(memberList);
    }

    @Operation(summary = "회원 배송지 저장", description = "회원 한 명에 대한 배송지를 저장한다.")
    @PostMapping("/v1/members/addresses/{id}")
    public ResponseEntity<SaveResponseDTO> addrSave(@Validated @RequestBody CreateAddressRequest createAddressRequest,
                                                    @PathVariable("id") Long memberId) {
        log.info("회원 배송지 저장 요청 데이터 : {}", createAddressRequest.toString());

        Long addressId = addressServiceApplication.addrSave(memberId, createAddressRequest);

        SaveResponseDTO saveResponseDTO = SaveResponseDTO.builder()
                .id(addressId)
                .statusCode(HttpStatus.OK.value())
                .message("addressId : " + addressId + " 회원 배송지 주소가 정상적으로 저장 되었습니다.")
                .build();

        return new ResponseEntity<>(saveResponseDTO, HttpStatus.CREATED);
    }
}