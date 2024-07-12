package org.store.clothstar.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.store.clothstar.product.entity.ProductEntity;

import java.util.List;

@Repository
public interface ProductJPARepository extends JpaRepository<ProductEntity, Long>, ProductRepositoryCustom {

    List<ProductEntity> findAllByProductId(Long productId);

    List<ProductEntity> findByIdIn(List<Long> productIds);
}
