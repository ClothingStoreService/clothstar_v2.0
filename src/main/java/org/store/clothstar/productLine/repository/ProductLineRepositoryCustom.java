package org.store.clothstar.productLine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsJPAResponse;

import java.util.Optional;

@Repository
public interface ProductLineRepositoryCustom {
    Page<ProductLineWithProductsJPAResponse> getProductLinesWithOptions(Pageable pageable);

    Optional<ProductLineWithProductsJPAResponse> findProductLineWithOptionsById(Long productLineId);

}
