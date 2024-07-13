package org.store.clothstar.productLine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.entity.ProductLineEntity;

import java.util.List;

@Repository
public interface ProductLineJPARepository extends JpaRepository<ProductLineEntity, Long>, ProductLineRepositoryCustom {

    List<ProductLineEntity> findByDeletedAtIsNullAndStatusNotIn(List<ProductLineStatus> statuses);

    List<ProductLineEntity> findByIdIn(List<Long> productLineIds);
}
