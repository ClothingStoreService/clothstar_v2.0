package org.store.clothstar.productLine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsJPAResponse;
import org.store.clothstar.productLine.entity.ProductLineEntity;

import java.util.Optional;

@Repository
public interface ProductLineRepositoryCustom {
    Page<ProductLineWithProductsJPAResponse> getProductLinesWithOptions(Pageable pageable);

    Optional<ProductLineWithProductsJPAResponse> findProductLineWithOptionsById(Long productLineId);

    Page<ProductLineWithProductsJPAResponse> findAllOffsetPaging(Pageable pageable, String keyword);

    Slice<ProductLineWithProductsJPAResponse> findAllSlicePaging(Pageable pageable, String keyword);

    Page<ProductLineEntity> findEntitiesByCategoryWithOffsetPaging(Long categoryId, Pageable pageable, String keyword);

    Slice<ProductLineEntity> findEntitiesByCategoryWithSlicePaging(Long categoryId, Pageable pageable, String keyword);
}
