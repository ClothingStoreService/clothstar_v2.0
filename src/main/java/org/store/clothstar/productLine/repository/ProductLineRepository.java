package org.store.clothstar.productLine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;

import java.util.List;

@Repository
public interface ProductLineRepository extends JpaRepository<ProductLine, Long>, ProductLineRepositoryCustom {

    List<ProductLine> findByDeletedAtIsNullAndStatusNotIn(List<ProductLineStatus> statuses);

    List<ProductLine> findByProductLineIdIn(List<Long> productLineIds);
}
