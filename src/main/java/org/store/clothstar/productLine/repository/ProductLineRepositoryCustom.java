package org.store.clothstar.productLine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import org.store.clothstar.productLine.domain.ProductLine;

import java.util.Optional;

@Repository
public interface ProductLineRepositoryCustom {
    Page<ProductLine> getProductLinesWithOptions(Pageable pageable);

    Optional<ProductLine> findProductLineWithOptionsById(Long productLineId);

    Page<ProductLine> findAllOffsetPaging(Pageable pageable, String keyword);

    Slice<ProductLine> findAllSlicePaging(Pageable pageable, String keyword);

    Page<ProductLine> findEntitiesByCategoryWithOffsetPaging(Long categoryId, Pageable pageable, String keyword);

    Slice<ProductLine> findEntitiesByCategoryWithSlicePaging(Long categoryId, Pageable pageable, String keyword);
}
