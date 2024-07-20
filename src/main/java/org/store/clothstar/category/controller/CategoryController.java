package org.store.clothstar.category.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.store.clothstar.category.dto.request.CreateCategoryRequest;
import org.store.clothstar.category.dto.request.UpdateCategoryRequest;
import org.store.clothstar.category.dto.response.CategoryDetailResponse;
import org.store.clothstar.category.dto.response.CategoryResponse;
import org.store.clothstar.category.service.CategoryService;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.common.util.URIBuilder;
import org.store.clothstar.productLine.dto.response.ProductLineDetailResponse;
import org.store.clothstar.productLine.service.ProductLineService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductLineService  productLineService;

    @Operation(summary = "전체 카테고리 조회", description = "모든 카테고리를 조회한다.")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categoryResponses = categoryService.getAllCategories();
        return ResponseEntity.ok().body(categoryResponses);
    }

    @Operation(summary = "카테고리 상세 조회", description = "id로 카테고리 한개를 상세 조회한다.")
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDetailResponse> getCategory(@PathVariable Long categoryId) {
        CategoryDetailResponse categoryDetailResponse = categoryService.getCategory(categoryId);
        return ResponseEntity.ok().body(categoryDetailResponse);
    }

    @Operation(summary = "카테고리 등록", description = "카테고리 타입(이름)을 입력하여 신규 카테고리를 등록한다.")
    @PostMapping
    public ResponseEntity<Long> createCategory(@Validated @RequestBody CreateCategoryRequest createCategoryRequest) {
        Long categoryId = categoryService.createCategory(createCategoryRequest);

        URI location = URIBuilder.buildURI(categoryId);

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "카테고리 수정", description = "카테고리 이름을 수정한다.")
    @PutMapping("/{categoryId}")
    public ResponseEntity<MessageDTO> updateCategories(
            @PathVariable Long categoryId,
            @Validated @RequestBody UpdateCategoryRequest updateCategoryRequest) {

        categoryService.updateCategory(categoryId, updateCategoryRequest);

        return ResponseEntity.ok().body(new MessageDTO(HttpStatus.OK.value(), "Category updated successfully"));
    }

    @Operation(summary = "카테고리별 상품 조회 (Offset Paging)", description = "카테고리 ID로 해당 카테고리에 속하는 모든 상품을 Offset Paging을 통해 조회한다.")
    @GetMapping("/{categoryId}/productLines/offset")
    public ResponseEntity<Page<ProductLineDetailResponse>> getProductLinesByCategory(
            @PathVariable Long categoryId,
            @PageableDefault(size = 18) Pageable pageable,
            @RequestParam(required = false) String keyword) {
        Page<ProductLineDetailResponse> productLineResponses = productLineService.getProductLinesByCategoryWithOffsetPaging(categoryId, pageable, keyword);
        return ResponseEntity.ok().body(productLineResponses);
    }

    @Operation(summary = "카테고리별 상품 조회 (Slice Paging)", description = "카테고리 ID로 해당 카테고리에 속하는 모든 상품을 Slice Paging을 통해 조회한다.")
    @GetMapping("/{categoryId}/productLines/slice")
    public ResponseEntity<Slice<ProductLineDetailResponse>> getProductLinesByCategorySlice(
            @PathVariable Long categoryId,
            @PageableDefault(size = 18) Pageable pageable,
            @RequestParam(required = false) String keyword) {
        Slice<ProductLineDetailResponse> productLineResponses = productLineService.getProductLinesByCategoryWithSlicePaging(categoryId, pageable, keyword);
        return ResponseEntity.ok().body(productLineResponses);
    }
}