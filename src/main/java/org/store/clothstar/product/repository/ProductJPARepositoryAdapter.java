package org.store.clothstar.product.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductJPARepositoryAdapter implements UpperProductRepository {

    private final ProductJPARepository productJPARepository;
    private final ProductLineJPARepository productLineJPARepository;

    public List<Product> selectAllProductByProductLineId(Long productId) {
        return productJPARepository.findAllByProductId(productId)
                .stream()
                .map(Product::from)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> selectByProductId(Long productId) {
        return productJPARepository.findById(productId)
                .map(Product::from);
    }

    @Override
    public int save(Product product) {
        ProductEntity productEntity = convertToProductEntity(product);
        productJPARepository.save(productEntity);
        return 1;
    }

    @Override
    public int updateProduct(Product product) {
        ProductEntity productEntity = productJPARepository.findById(product.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + product.getProductLineId()));

        productEntity.updateOption(product);

        return 1;
    }

    @Override
    public int deleteProduct(Long productId) {
        ProductEntity productEntity = productJPARepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + productId));

        productJPARepository.delete(productEntity);
        return 0;
    }

    private ProductEntity convertToProductEntity(Product product) {
        ProductLineEntity productLine = productLineJPARepository.findById(product.getProductLineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ProductLine not found with id: " + product.getProductLineId()));

        return ProductEntity.builder()
                .productId(product.getProductId())
                .productLine(productLine)
                .name(product.getName())
                .extraCharge(product.getExtraCharge())
                .stock(product.getStock())
                .build();
    }
}
