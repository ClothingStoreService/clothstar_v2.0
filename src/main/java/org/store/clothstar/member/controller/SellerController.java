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
import org.store.clothstar.member.application.SellerServiceApplication;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.dto.response.SellerResponse;
import org.store.clothstar.member.entity.SellerEntity;

@Tag(name = "Seller", description = "판매자 정보 관리에 대한 API 입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
public class SellerController {
    private final SellerServiceApplication sellerServiceApplication;

    @Operation(summary = "판매자 상세정보 조회", description = "판매자 한 명에 대한 상세정보를 가져온다.")
    @GetMapping("/v1/sellers/{id}")
    public ResponseEntity<SellerResponse> getSeller(@PathVariable("id") Long memberId) {
        SellerEntity sellerEntity = sellerServiceApplication.getSellerById(memberId);
        return ResponseEntity.ok(new SellerResponse(sellerEntity));
    }

    @Operation(summary = "판매자 가입", description = "판매자 정보를 저장된다.")
    @PostMapping("/v1/sellers/{id}")
    public ResponseEntity<SaveResponseDTO> saveSeller(@Validated @RequestBody CreateSellerRequest createSellerRequest,
                                                      @PathVariable("id") Long memberId) {
        log.info("판매자 가입 요청 데이터 : {}", createSellerRequest.toString());
        Long sellerId = sellerServiceApplication.sellerSave(memberId, createSellerRequest);

        SaveResponseDTO saveResponseDTO = SaveResponseDTO.builder()
                .id(sellerId)
                .statusCode(HttpStatus.OK.value())
                .message("memberId : " + sellerId + " 가 판매자 가입이 정상적으로 되었습니다.")
                .build();

        return new ResponseEntity<>(saveResponseDTO, HttpStatus.CREATED);
    }
}