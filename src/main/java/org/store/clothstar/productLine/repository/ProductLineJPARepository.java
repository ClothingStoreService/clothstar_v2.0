package org.store.clothstar.productLine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.clothstar.productLine.entity.ProductLineEntity;

public interface ProductLineJPARepository extends JpaRepository<ProductLineEntity, Long>, ProductLineRepositoryCustom {
}
