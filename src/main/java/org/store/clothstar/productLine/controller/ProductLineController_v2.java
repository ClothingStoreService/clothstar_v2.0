package org.store.clothstar.productLine.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.common.util.URIBuilder;
import org.store.clothstar.productLine.application.ProductLineApplicationService;
import org.store.clothstar.productLine.dto.request.CreateProductLineRequest;
import org.store.clothstar.productLine.dto.request.UpdateProductLineRequest;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsJPAResponse;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsResponse;

import java.net.URI;

@Tag(name = "ProductLines-v2", description = "ProductLine 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/productLines")
public class ProductLineController_v2 {

    private final ProductLineApplicationService productLineApplicationService;

    @Operation(summary = "전체 상품 조회", description = "삭제되지 않은 모든 상품을 조회한다.")
    @GetMapping
    public ResponseEntity<Page<ProductLineWithProductsJPAResponse>> getAllProductLines(Pageable pageable) {
        Page<ProductLineWithProductsJPAResponse> productLineResponses = productLineApplicationService.getAllProductLinesWithProducts(pageable);
        return ResponseEntity.ok().body(productLineResponses);
    }

    @Operation(summary = "상품 상세 조회", description = "productLineId로 상품과 하위 옵션들을 상세 조회한다.")
    @GetMapping("/{productLineId}")
    public ResponseEntity<ProductLineWithProductsJPAResponse> getProductLine(@PathVariable("productLineId") Long productLineId) {
        ProductLineWithProductsJPAResponse productLineWithProducts = productLineApplicationService.getProductLineWithProductsById(productLineId);
        return ResponseEntity.ok().body(productLineWithProducts);
    }

    @Operation(summary = "상품 등록", description = "카테고리 아이디, 상품 이름, 내용, 가격, 상태를 입력하여 상품을 신규 등록한다.")
    @PostMapping
    public ResponseEntity<URI> createProductLine(@Validated @RequestBody CreateProductLineRequest createProductLineRequest) {
        Long productLineId = productLineApplicationService.createProductLine(createProductLineRequest);
        URI location = URIBuilder.buildURI(productLineId);

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "상품 수정", description = "상품 이름, 가격, 재고, 상태를 입력하여 상품 정보를 수정한다.")
    @PutMapping("/{productLineId}")
    public ResponseEntity<MessageDTO> updateProductLine(
            @PathVariable Long productLineId,
            @Validated @RequestBody UpdateProductLineRequest updateProductLineRequest) {

        productLineApplicationService.updateProductLine(productLineId, updateProductLineRequest);

        return ResponseEntity.ok().body(new MessageDTO(HttpStatus.OK.value(), "ProductLine updated successfully", null));
    }

    @DeleteMapping("/{productLineId}")
    public ResponseEntity<Void> deleteProductLine(@PathVariable Long productLineId) {
        productLineApplicationService.setDeletedAt(productLineId);
        return ResponseEntity.noContent().build();
    }

}
