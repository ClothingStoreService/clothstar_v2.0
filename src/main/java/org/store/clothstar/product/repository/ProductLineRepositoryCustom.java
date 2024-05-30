package org.store.clothstar.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsJPAResponse;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsResponse;

@Repository
public interface ProductLineRepositoryCustom {
    Page<ProductLineWithProductsJPAResponse> findProductLineWithOptions(Pageable pageable);
}
