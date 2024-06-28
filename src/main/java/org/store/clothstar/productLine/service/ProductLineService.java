package org.store.clothstar.productLine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.dto.request.CreateProductLineRequest;
import org.store.clothstar.productLine.dto.request.UpdateProductLineRequest;
import org.store.clothstar.productLine.dto.response.ProductLineResponse;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsResponse;
import org.store.clothstar.productLine.repository.UpperProductLineRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductLineService {

    private final UpperProductLineRepository productLineRepository;

    @Transactional(readOnly = true)
    public List<ProductLineResponse> getAllProductLines() {
        return productLineRepository.selectAllProductLinesNotDeleted().stream()
                .map(ProductLineResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ProductLineResponse> getProductLine(Long productLineId) {
        return productLineRepository.selectByProductLineId(productLineId)
                .map(ProductLineResponse::from);
    }

    @Transactional(readOnly = true)
    public ProductLineWithProductsResponse getProductLineWithProducts(Long productLineId) {
        ProductLineWithProductsResponse productLineWithProducts = productLineRepository.selectProductLineWithOptions(productLineId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "productLineId :" + productLineId + "인 상품 및 옵션 정보를 찾을 수 없습니다."));
        List<Product> productList = productLineWithProducts.getProductList();

        Long totalStock = 0L;
        for (Product product : productList) {
            totalStock += product.getStock();
        }

        productLineWithProducts.setTotalStock(totalStock);

        return productLineWithProducts;
    }

    @Transactional
    public Long createProductLine(CreateProductLineRequest createProductLineRequest) {
        Long memberId = 1L;
        ProductLine product = createProductLineRequest.toProductLine(memberId);
        productLineRepository.save(product);
        return product.getProductLineId();
    }

    @Transactional
    public void updateProductLine(Long productLineId, UpdateProductLineRequest updateProductLineRequest) {
        ProductLine productLine = productLineRepository.selectByProductLineId(productLineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품 정보를 찾을 수 없습니다."));

        productLine.updateProductLine(updateProductLineRequest);

        productLineRepository.updateProductLine(productLine);
    }

    @Transactional
    public void setDeletedAt(Long productId) {
        ProductLine productLine = productLineRepository.selectByProductLineId(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품 정보를 찾을 수 없습니다."));

        productLine.setDeletedAt();

        ProductLine prodcutLine = productLineRepository.selectByProductLineId(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품 정보를 찾을 수 없습니다."));

        prodcutLine.setDeletedAt();

        productLineRepository.setDeletedAt(prodcutLine);
    }
}