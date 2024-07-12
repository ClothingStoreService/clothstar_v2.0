package org.store.clothstar.productLine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.category.entity.CategoryEntity;
import org.store.clothstar.category.repository.CategoryJpaRepository;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.repository.SellerRepository;
import org.store.clothstar.product.dto.response.ProductResponse;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.dto.request.CreateProductLineRequest;
import org.store.clothstar.productLine.dto.request.UpdateProductLineRequest;
import org.store.clothstar.productLine.dto.response.ProductLineResponse;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsJPAResponse;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductLineService {

    private final ProductLineJPARepository productLineRepository;
    private final SellerRepository sellerRepository;
    private final CategoryJpaRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ProductLineResponse> getAllProductLines() {
        return productLineRepository.findByDeletedAtIsNullAndStatusNotIn(
                        Arrays.asList(ProductLineStatus.HIDDEN))
                .stream()
                .map(ProductLineResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ProductLineWithProductsJPAResponse> getAllProductLinesWithProductsOffsetPaging(Pageable pageable, String keyword) {
        return productLineRepository.findAllOffsetPaging(pageable, keyword);
    }

    @Transactional(readOnly = true)
    public Slice<ProductLineWithProductsJPAResponse> getAllProductLinesWithProductsSlicePaging(Pageable pageable, String keyword) {
        return productLineRepository.findAllSlicePaging(pageable, keyword);
    }

    @Transactional(readOnly = true)
    public Optional<ProductLineResponse> getProductLine(Long productLineId) {
        return productLineRepository.findById(productLineId)
                .map(ProductLineResponse::from);
    }

    @Transactional(readOnly = true)
    public ProductLineWithProductsJPAResponse getProductLineWithProducts(Long productLineId) {
        ProductLineWithProductsJPAResponse productLineWithProducts =
                productLineRepository.findProductLineWithOptionsById(productLineId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "productLineId :" + productLineId + "인 상품 및 옵션 정보를 찾을 수 없습니다."));


        return productLineWithProducts;
    }

    @Transactional
    public Page<ProductLineWithProductsJPAResponse> getProductLinesByCategoryWithOffsetPaging(Long categoryId, Pageable pageable, String keyword) {
        Page<ProductLineEntity> productLineEntities = productLineRepository.findEntitiesByCategoryWithOffsetPaging(categoryId, pageable, keyword);
        return productLineEntities.map(this::convertToDtoWithProducts);
    }

    @Transactional
    public Slice<ProductLineWithProductsJPAResponse> getProductLinesByCategoryWithSlicePaging(Long categoryId, Pageable pageable, String keyword) {
        Slice<ProductLineEntity> productLineEntities = productLineRepository.findEntitiesByCategoryWithSlicePaging(categoryId, pageable, keyword);
        return productLineEntities.map(this::convertToDtoWithProducts);
    }

    @Transactional
    public Long createProductLine(CreateProductLineRequest createProductLineRequest) {
        Long memberId = 1L;
        Seller seller = sellerRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "판매자 정보를 찾을 수 없습니다."));

        CategoryEntity category = categoryRepository.findById(createProductLineRequest.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."));

        ProductLineEntity productLine = createProductLineRequest.toProductLineEntity(seller, category);
        ProductLineEntity savedProductLine = productLineRepository.save(productLine);
        return savedProductLine.getProductLineId();
    }

    @Transactional
    public void updateProductLine(Long productLineId, UpdateProductLineRequest updateProductLineRequest) {
        ProductLineEntity productLine = productLineRepository.findById(productLineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품 정보를 찾을 수 없습니다."));

        productLine.updateProductLine(updateProductLineRequest);
    }

    @Transactional
    public void setDeletedAt(Long productId) {
        ProductLineEntity productLine = productLineRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품 정보를 찾을 수 없습니다."));

        productLine.delete();
    }

    private ProductLineWithProductsJPAResponse convertToDtoWithProducts(ProductLineEntity productLine) {
        // 전체 재고량 계산
        Long totalStock = productLine.getProducts().stream().mapToLong(ProductEntity::getStock).sum();

        // ProductLineWithProductsJPAResponse 객체 생성
        ProductLineWithProductsJPAResponse dto = new ProductLineWithProductsJPAResponse(
                productLine,
                productLine.getSeller(),
                totalStock
        );

        // productList 설정
        List<ProductResponse> productResponses = productLine.getProducts().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        dto.setProductList(productResponses);

        return dto;
    }
}