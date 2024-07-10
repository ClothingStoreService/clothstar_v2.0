package org.store.clothstar.product.repository;

import org.springframework.stereotype.Repository;
import org.store.clothstar.product.domain.Product;

import java.util.List;
import java.util.Optional;
@Repository
public class ProductRepositoryImpl implements ProductRepository
{
    @Override
    public List<Product> selectAllProductByProductLineId(Long productId) {
        return List.of();
    }

    @Override
    public Optional<Product> selectByProductId(Long productId) {
        return Optional.empty();
    }

    @Override
    public int save(Product product) {
        return 0;
    }

    @Override
    public int updateProduct(Product product) {
        return 0;
    }

    @Override
    public int deleteProduct(Long productId) {
        return 0;
    }
}
