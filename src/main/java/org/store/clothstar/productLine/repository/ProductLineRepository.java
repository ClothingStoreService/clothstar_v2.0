package org.store.clothstar.productLine.repository;

import org.springframework.stereotype.Repository;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsResponse;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductLineRepository {

    List<ProductLine> selectAllProductLinesNotDeleted();

    Optional<ProductLine> selectByProductLineId(Long productId);

    Optional<ProductLineWithProductsResponse> findProductLineWithOptionsById(Long productId);
}
