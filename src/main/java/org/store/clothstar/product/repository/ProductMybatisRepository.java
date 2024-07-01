package org.store.clothstar.product.repository;

import org.apache.ibatis.annotations.Mapper;
import org.store.clothstar.product.domain.Product;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProductMybatisRepository extends ProductRepository {

    List<Product> selectAllProductsById(Long productId);

    Optional<Product> selectByProductId(Long productId);

//    Optional<ProductLineWithProductsResponse> selectProductLineWithOptions(Long productId);

    int save(Product product);

    int updateProduct(Product product);

    int deleteProduct(Long productId);

}
