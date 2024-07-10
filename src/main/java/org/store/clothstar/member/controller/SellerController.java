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
import org.store.clothstar.member.application.SellerServiceApplication;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.dto.response.MemberResponse;
import org.store.clothstar.member.dto.response.SellerResponse;

@Tag(name = "Seller", description = "판매자 정보 관리에 대한 API 입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
public class SellerController {
    private final SellerServiceApplication sellerServiceApplication;

    @Operation(summary = "판매자 상세정보 조회", description = "판매자 한 명에 대한 상세정보를 가져온다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "판매자 상세정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/v1/sellers/{id}")
    public ResponseEntity<SellerResponse> getSeller(@PathVariable("id") Long memberId) {
        Seller seller = sellerServiceApplication.getSellerById(memberId);
        return ResponseEntity.ok(new SellerResponse(seller));
    }

    @Operation(summary = "판매자 가입", description = "회원이 판매자를 신청한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "판매자 신청이 정상적으로 되었습니다.",
                    content = @Content(schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원 정보를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "이미 판매자 가입이 되어 있습니다., 이미 존재하는 사업자 번호 입니다., 이미 존재하는 브랜드 이름 입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/v1/sellers/{id}")
    public ResponseEntity<MessageDTO> saveSeller(@Validated @RequestBody CreateSellerRequest createSellerRequest,
                                                 @PathVariable("id") Long memberId) {
        log.info("판매자 가입 요청 데이터 : {}", createSellerRequest.toString());
        sellerServiceApplication.sellerSave(memberId, createSellerRequest);

        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(
                HttpStatus.CREATED.value(),
                "판매자 신청이 정상적으로 되었습니다."
        );

        return new ResponseEntity<>(messageDTO, HttpStatus.CREATED);
    }
}