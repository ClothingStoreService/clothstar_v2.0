package org.store.clothstar.product.repository;

import org.apache.ibatis.annotations.Mapper;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsResponse;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductRepository {

    List<Product> selectAllProducts();

    Optional<Product> selectByProductId(Long productId);

    Optional<ProductLineWithProductsResponse> selectProductLineWithOptions(Long productId);

    int save(Product product);

    int updateProduct(Product product);

    int deleteProduct(Long productId);

}
