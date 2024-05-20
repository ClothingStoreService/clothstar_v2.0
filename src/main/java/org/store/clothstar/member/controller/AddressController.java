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
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.dto.response.AddressResponse;
import org.store.clothstar.member.service.AddressService;

import java.util.List;

@Tag(name = "Address", description = "회원 배송지 주소 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AddressController {
    private final AddressService addressService;

    @Operation(summary = "상품 옵션 상세 조회", description = "회원 한 명에 대한 배송지를 전부 가져온다.")
    @GetMapping("/v1/members/{id}/address")
    public ResponseEntity<List<AddressResponse>> getMemberAllAddress(@PathVariable("id") Long memberId) {
        List<AddressResponse> memberList = addressService.getMemberAllAddress(memberId);
        return ResponseEntity.ok(memberList);
    }

    @Operation(summary = "회원 배송지 저장", description = "회원 한 명에 대한 배송지를 저장한다.")
    @PostMapping("/v1/members/{id}/address")
    public ResponseEntity<MessageDTO> addrSave(@Validated @RequestBody CreateAddressRequest createAddressRequest,
                                               @PathVariable("id") Long memberId) {
        log.info("회원 배송지 저장 요청 데이터 : {}", createAddressRequest.toString());
        return new ResponseEntity<>(addressService.addrSave(memberId, createAddressRequest), HttpStatus.CREATED);
    }
}