package org.store.clothstar.product.repository;

import org.store.clothstar.product.domain.Product;

import java.util.List;
import java.util.Optional;

public interface UpperProductRepository {

    List<Product> selectAllProductByProductLineId(Long productId);

    Optional<Product> selectByProductId(Long productId);

    int save(Product product);

    int updateProduct(Product product);

    int deleteProduct(Long productId);
}
