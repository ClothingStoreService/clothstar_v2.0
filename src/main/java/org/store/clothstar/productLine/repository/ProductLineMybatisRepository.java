package org.store.clothstar.productLine.repository;

import org.apache.ibatis.annotations.Mapper;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsResponse;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductLineMybatisRepository {

    List<ProductLine> selectAllProductLinesNotDeleted();
    
    Optional<ProductLine> selectByProductLineId(Long productId);

    Optional<ProductLineWithProductsResponse> selectProductLineWithOptions(Long productId);

    int save(ProductLine productLine);

    int updateProductLine(ProductLine productLine);

    int setDeletedAt(ProductLine productLine);
}