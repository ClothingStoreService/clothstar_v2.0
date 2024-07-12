package org.store.clothstar.productLine.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.common.util.URIBuilder;
import org.store.clothstar.productLine.dto.request.CreateProductLineRequest;
import org.store.clothstar.productLine.dto.request.UpdateProductLineRequest;
import org.store.clothstar.productLine.dto.response.ProductLineResponse;
import org.store.clothstar.productLine.dto.response.ProductLineDetailResponse;
import org.store.clothstar.productLine.service.ProductLineService;

import java.net.URI;
import java.util.List;

@Tag(name = "ProductLines", description = "ProductLine 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
//@RequestMapping("/v1/productLines")
public class ProductLineController {

    private final ProductLineService productLineService;

    @Deprecated
    @Operation(summary = "전체 상품 조회", description = "삭제되지 않은 모든 상품을 조회한다.")
    @GetMapping("/v1/productLines")
    public ResponseEntity<List<ProductLineResponse>> getAllProductLines() {
        List<ProductLineResponse> productLineResponses = productLineService.getAllProductLines();
        return ResponseEntity.ok().body(productLineResponses);
    }

    @Operation(summary = "전체 상품 Offset Paging 조회", description = "삭제되지 않은 모든 상품을 조회한다.")
    @GetMapping("/v1/productLines/offset")
    public ResponseEntity<Page<ProductLineDetailResponse>> getAllProductLinesOffsetPaging(
            @PageableDefault(size = 18) Pageable pageable,
            @RequestParam(required = false) String keyword){
        Page<ProductLineDetailResponse> productLineResponses = productLineService.getAllProductLinesWithProductsOffsetPaging(pageable, keyword);
        return ResponseEntity.ok().body(productLineResponses);
    }

    @Operation(summary = "전체 상품 Slice Paging 조회", description = "삭제되지 않은 모든 상품을 조회한다.")
    @GetMapping("/v1/productLines/slice")
    public ResponseEntity<Slice<ProductLineDetailResponse>> getAllProductLinesSlicePaging(
            @PageableDefault(size = 18) Pageable pageable,
            @RequestParam(required = false) String keyword) {
        Slice<ProductLineDetailResponse> productLineResponses = productLineService.getAllProductLinesWithProductsSlicePaging(pageable, keyword);
        return ResponseEntity.ok().body(productLineResponses);
    }

    @Operation(summary = "상품 상세 조회", description = "productLineId로 상품과 하위 옵션들을 상세 조회한다.")
    @GetMapping("/v1/productLines/{productLineId}")
    public ResponseEntity<ProductLineDetailResponse> getProductLine(@PathVariable("productLineId") Long productLineId) {
        ProductLineDetailResponse productLineWithProducts = productLineService.getProductLineWithProducts(productLineId);
        return ResponseEntity.ok().body(productLineWithProducts);
    }

    @Operation(summary = "상품 등록", description = "카테고리 아이디, 상품 이름, 내용, 가격, 상태를 입력하여 상품을 신규 등록한다.")
    @PostMapping("/v1/productLines")
    public ResponseEntity<URI> createProductLine(@Validated @RequestBody CreateProductLineRequest createProductLineRequest) {
        Long productLineId = productLineService.createProductLine(createProductLineRequest);
        URI location = URIBuilder.buildURI(productLineId);

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "상품 수정", description = "상품 이름, 가격, 재고, 상태를 입력하여 상품 정보를 수정한다.")
    @PutMapping("/v1/productLines/{productLineId}")
    public ResponseEntity<MessageDTO> updateProductLine(
            @PathVariable Long productLineId,
            @Validated @RequestBody UpdateProductLineRequest updateProductLineRequest) {

        productLineService.updateProductLine(productLineId, updateProductLineRequest);

        return ResponseEntity.ok().body(new MessageDTO(HttpStatus.OK.value(), "ProductLine updated successfully"));
    }

    @Operation(summary = "상품 삭제", description = "productLineId를 통해 deletedAt 컬럼을 설정해 soft delete를 수행한다.")
    @DeleteMapping("/v1/productLines/{productLineId}")
    public ResponseEntity<Void> deleteProductLine(@PathVariable("productLineId") Long productLineId) {
        productLineService.setDeletedAt(productLineId);
        return ResponseEntity.noContent().build();
    }
}