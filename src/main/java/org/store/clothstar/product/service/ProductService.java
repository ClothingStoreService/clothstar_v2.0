package org.store.clothstar.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.product.dto.request.CreateProductRequest;
import org.store.clothstar.product.dto.request.UpdateProductRequest;
import org.store.clothstar.product.dto.response.ProductResponse;
import org.store.clothstar.product.repository.ProductRepository;
import org.store.clothstar.product.repository.UpperProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final UpperProductRepository productRepository;

    /*
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProduct() {
        return productRepository.selectAllProducts().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
     */
    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long productId) {
        return productRepository.selectByProductId(productId)
                .map(ProductResponse::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "productId :" + productId + "인 상품 옵션 정보를 찾을 수 없습니다."));
    }

    @Transactional
    public Long createProduct(@Validated @RequestBody CreateProductRequest createProductRequest) {
        Product product = createProductRequest.toProduct();
        productRepository.save(product);

        return product.getProductLineId();
    }

    @Transactional
    public void updateProduct(Long productId, UpdateProductRequest updateProductRequest) {
        Product product = productRepository.selectByProductId(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "productId :" + productId + "인 상품 옵션 정보를 찾을 수 없습니다."));

        product.updateOption(updateProductRequest);

        productRepository.updateProduct(product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.selectByProductId(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "productId :" + productId + "인 상품 옵션 정보를 찾을 수 없습니다."));

        productRepository.deleteProduct(productId);
    }
}
