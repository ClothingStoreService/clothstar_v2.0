package org.store.clothstar.productLine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.category.domain.Category;
import org.store.clothstar.category.repository.CategoryRepository;
import org.store.clothstar.member.entity.SellerEntity;
import org.store.clothstar.productLine.dto.request.CreateProductLineRequest;
import org.store.clothstar.productLine.dto.request.UpdateProductLineRequest;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsJPAResponse;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.exception.ProductLineException;
import org.store.clothstar.productLine.exception.ProductLineExceptionType;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductLineServiceV2 {

    private final ProductLineJPARepository productLineJPARepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductLineWithProductsJPAResponse> getAllProductLinesWithProducts(Pageable pageable) {
        return productLineJPARepository.getProductLinesWithOptions(pageable);
    }

    @Transactional(readOnly = true)
    public ProductLineWithProductsJPAResponse getProductLineWithProductsById(Long productLineId) {
        return productLineJPARepository.findProductLineWithOptionsById(productLineId)
                .orElseThrow(() -> new ProductLineException(ProductLineExceptionType.NOT_FOUND_EXCEPTION));
    }

//    @Transactional(readOnly = true)
//    public ProductLineWithProductsJPAResponse getProductLineWithProducts(Long productLineId) {
//        ProductLineWithProductsJPAResponse productLineWithProducts = productLineJPARepository.selectProductLineWithOptions(productLineId)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.BAD_REQUEST,
//                        "productLineId :" + productLineId + "인 상품 및 옵션 정보를 찾을 수 없습니다."));
//        List<Product> productList = productLineWithProducts.getProductList();
//
//        Long totalStock = 0L;
//        for (Product product : productList) {
//            totalStock += product.getStock();
//        }
//
//        productLineWithProducts.setTotalStock(totalStock);
//
//        return productLineWithProducts;
//    }

    @Transactional
    public Long createProductLine(CreateProductLineRequest createProductLineRequest) {
        Long memberId = 1L;
        SellerEntity seller = null;  // TODO: Seller 가져오기
        Category category = categoryRepository.selectCategoryById(createProductLineRequest.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."));
        ProductLineEntity product = createProductLineRequest.toProductLineEntity(memberId, seller, category);
        productLineJPARepository.save(product);
        return product.getProductLineId();
    }

    @Transactional
    public void updateProductLine(Long productLineId, UpdateProductLineRequest updateProductLineRequest) {
        ProductLineEntity productLine = productLineJPARepository.findById(productLineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."));

        productLine.updateProductLine(updateProductLineRequest);
    }

    @Transactional
    public void setDeletedAt(Long productId) {
        ProductLineEntity productLine = productLineJPARepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."));

        productLine.delete();
    }

}
