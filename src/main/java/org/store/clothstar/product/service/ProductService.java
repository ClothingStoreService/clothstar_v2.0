package org.store.clothstar.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.orderDetail.domain.OrderDetail;
import org.store.clothstar.product.dto.request.CreateProductRequest;
import org.store.clothstar.product.dto.request.UpdateProductRequest;
import org.store.clothstar.product.dto.response.ProductResponse;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.repository.ProductJPARepository;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductJPARepository productJPARepository;

    private final ProductJPARepository productRepository;
    private final ProductLineJPARepository productLineRepository;


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
        return productRepository.findById(productId)
                .map(ProductResponse::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "productId :" + productId + "인 상품 옵션 정보를 찾을 수 없습니다."));
    }

    @Transactional
    public Long createProduct(@Validated @RequestBody CreateProductRequest createProductRequest) {
        ProductLineEntity ProductLine = productLineRepository.findById(createProductRequest.getProductLineId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "productLineId :" + createProductRequest.getProductLineId() + "인 상품 라인 정보를 찾을 수 없습니다."));

        ProductEntity product = createProductRequest.toProductEntity(ProductLine);
        ProductEntity savedProduct = productRepository.save(product);

        return savedProduct.getProductId();
    }

    @Transactional
    public void updateProduct(Long productId, UpdateProductRequest updateProductRequest) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "productId :" + productId + "인 상품 옵션 정보를 찾을 수 없습니다."));

        product.updateOption(updateProductRequest);
    }

    @Transactional
    public void deleteProduct(Long productId) {

        productRepository.deleteById(productId);
    }

    @Transactional
    public void restoreProductStockByOrder(List<OrderDetail> orderDetailList) {
        orderDetailList.forEach(orderDetail -> {
            ProductEntity productEntity = productRepository.findById(orderDetail.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."));
            productEntity.restoreStock(orderDetail.getQuantity());
        });
    }

    public void restoreProductStockByOrderDetail(OrderDetail orderDetail) {
        ProductEntity productEntity = productRepository.findById(orderDetail.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."));
        productEntity.restoreStock(orderDetail.getQuantity());
    }

    public List<ProductEntity> findByIdIn(List<Long> productIds) {
        return productJPARepository.findByIdIn(productIds);
    }
}
