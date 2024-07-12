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
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.dto.request.CreateProductLineRequest;
import org.store.clothstar.productLine.dto.request.UpdateProductLineRequest;
import org.store.clothstar.productLine.dto.response.ProductLineResponse;
import org.store.clothstar.productLine.dto.response.ProductLineDetailResponse;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;

import java.util.Arrays;
import java.util.List;
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
    public Page<ProductLineDetailResponse> getAllProductLinesWithProductsOffsetPaging(Pageable pageable, String keyword) {
        Page<ProductLineEntity> allOffsetPaging = productLineRepository.findAllOffsetPaging(pageable, keyword);

        return allOffsetPaging.map(this::convertToDtoWithProducts);
    }

    @Transactional(readOnly = true)
    public Slice<ProductLineDetailResponse> getAllProductLinesWithProductsSlicePaging(Pageable pageable, String keyword) {
        Slice<ProductLineEntity> allSlicePaging = productLineRepository.findAllSlicePaging(pageable, keyword);
        return allSlicePaging.map(this::convertToDtoWithProducts);
    }

    @Deprecated
    @Transactional(readOnly = true)
    public ProductLineDetailResponse getProductLineWithProducts(Long productLineId) {
        ProductLineEntity productLine = productLineRepository.findById(productLineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품 정보를 찾을 수 없습니다."));

        return convertToDtoWithProducts(productLine);
    }

    @Transactional
    public Page<ProductLineDetailResponse> getProductLinesByCategoryWithOffsetPaging(Long categoryId, Pageable pageable, String keyword) {
        Page<ProductLineEntity> allOffsetPagingByCategory = productLineRepository.findEntitiesByCategoryWithOffsetPaging(categoryId, pageable, keyword);

        return allOffsetPagingByCategory.map(this::convertToDtoWithProducts);
    }

    @Transactional
    public Slice<ProductLineDetailResponse> getProductLinesByCategoryWithSlicePaging(Long categoryId, Pageable pageable, String keyword) {
        Slice<ProductLineEntity> allSlicePagingByCategory = productLineRepository.findEntitiesByCategoryWithSlicePaging(categoryId, pageable, keyword);

        return allSlicePagingByCategory.map(this::convertToDtoWithProducts);
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

    private ProductLineDetailResponse convertToDtoWithProducts(ProductLineEntity productLine) {
        return new ProductLineDetailResponse(productLine);
    }

    public List<ProductLineEntity> findByIdIn(List<Long> productLineIds) {
        return productLineRepository.findByIdIn(productLineIds);
    }

    public Optional<ProductLineEntity> findById(Long productLineId) {
        return productLineRepository.findById(productLineId);
    }
}