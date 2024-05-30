package org.store.clothstar.product.repository;

import org.springframework.stereotype.Repository;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsResponse;

import java.util.Optional;

@Repository
public interface ProductLineRepositoryCustom {
    Optional<ProductLineWithProductsResponse> findProductLineWithOptions(Long productLineId);
}
