package org.store.clothstar.productLine.repository;

import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsResponse;

import java.util.List;
import java.util.Optional;

public interface UpperProductLineRepository {

    List<ProductLine> selectAllProductLinesNotDeleted();

    Optional<ProductLine> selectByProductLineId(Long productId);

    Optional<ProductLineWithProductsResponse> selectProductLineWithOptions(Long productId);

    int save(ProductLine productLine);

    int updateProductLine(ProductLine productLine);

    int setDeletedAt(ProductLine productLine);
}
