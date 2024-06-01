package org.store.clothstar.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.common.util.URIBuilder;
import org.store.clothstar.product.dto.request.CreateProductRequest;
import org.store.clothstar.product.dto.request.UpdateProductRequest;
import org.store.clothstar.product.dto.response.ProductResponse;
import org.store.clothstar.product.repository.ProductRepository;
import org.store.clothstar.product.service.ProductService;

import java.net.URI;

@Tag(name = "Products", description = "Products(상품 옵션) 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    /*
    @Operation(summary = "전체 상품 옵션 조회", description = "상품 Id의 모든 상품 옵션을 조회한다.")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> productResponses = productService.getAllProduct();
        return ResponseEntity.ok().body(productResponses);
    }
    */

    @Operation(summary = "상품 옵션 상세 조회", description = "productId로 상품 옵션 한개를 상세 조회한다.")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
        ProductResponse productResponse = productService.getProduct(productId);
        return ResponseEntity.ok().body(productResponse);
    }


    @Operation(summary = "상품 옵션 등록", description = "상품 옵션 이름, 추가금액, 재고 수를 입력하여 상품을 신규 등록한다.")
    @PostMapping
    public ResponseEntity<URI> createProduct(@Validated @RequestBody CreateProductRequest createProductRequest) {
        Long productId = productService.createProduct(createProductRequest);
        URI location = URIBuilder.buildURI(productId);

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "상품 옵션 수정", description = "상품 옵션 이름, 추가금액, 재고 수를 입력하여 상품 옵션 정보를 수정한다.")
    @PutMapping("/{productId}")
    public ResponseEntity<MessageDTO> updateProduct(
            @PathVariable Long productId,
            @Validated @RequestBody UpdateProductRequest updateProductRequest) {

        productService.updateProduct(productId, updateProductRequest);

        return ResponseEntity.ok().body(new MessageDTO(HttpStatus.OK.value(), "Product updated successfully"));
    }

    @Operation(summary = "상품 옵션 삭제", description = "상품 옵션 id로 상품 옵션을 삭제한다.")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
