package org.store.clothstar.productLine.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.store.clothstar.productLine.dto.request.CreateProductLineRequest;
import org.store.clothstar.productLine.dto.request.UpdateProductLineRequest;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsJPAResponse;
import org.store.clothstar.productLine.service.ProductLineServiceV2;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductLineApplicationService {

    private final ProductLineServiceV2 productLineServiceV2;

    public Page<ProductLineWithProductsJPAResponse> getAllProductLinesWithProducts(Pageable pageable) {
        return productLineServiceV2.getAllProductLinesWithProducts(pageable);
    }

    @Transactional(readOnly = true)
    public ProductLineWithProductsJPAResponse getProductLineWithProductsById(Long productLineId) {
        return productLineServiceV2.getProductLineWithProductsById(productLineId);
    }

    @Transactional
    public Long createProductLine(CreateProductLineRequest createProductLineRequest) {
        return productLineServiceV2.createProductLine(createProductLineRequest);
    }

    @Transactional
    public void updateProductLine(Long productLineId, UpdateProductLineRequest updateProductLineRequest) {
        productLineServiceV2.updateProductLine(productLineId, updateProductLineRequest);
    }

    @Transactional
    public void setDeletedAt(Long productId) {
        productLineServiceV2.setDeletedAt(productId);
    }
}
