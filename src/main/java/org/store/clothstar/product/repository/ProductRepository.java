package org.store.clothstar.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.store.clothstar.product.domain.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    List<Product> findAllByProductId(Long productId);

    List<Product> findByProductIdIn(List<Long> productIds);
}
