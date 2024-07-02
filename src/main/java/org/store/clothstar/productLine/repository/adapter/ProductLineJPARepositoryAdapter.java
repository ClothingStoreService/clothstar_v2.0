package org.store.clothstar.productLine.repository.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.category.domain.Category;
import org.store.clothstar.category.entity.CategoryEntity;
import org.store.clothstar.category.repository.CategoryJpaRepository;
import org.store.clothstar.member.entity.SellerEntity;
import org.store.clothstar.member.repository.SellerRepository;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.repository.ProductJPARepository;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.dto.response.ProductLineWithProductsResponse;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;
import org.store.clothstar.productLine.repository.ProductLineRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductLineJPARepositoryAdapter implements ProductLineRepository {
    private final CategoryJpaRepository categoryJpaRepository;
    private final SellerRepository sellerJpaRepository;

    private final ProductLineJPARepository productLineJPARepository;
    private final ProductJPARepository productJPARepository;


    @Override
    public List<ProductLine> selectAllProductLinesNotDeleted() {
        return productLineJPARepository.findByDeletedAtIsNullAndStatusNotIn(
                Arrays.asList(ProductLineStatus.HIDDEN, ProductLineStatus.DISCONTINUED))
                .stream()
                .map(ProductLine::from)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductLine> selectByProductLineId(Long productId) {
        return productLineJPARepository.findById(productId)
                .map(ProductLine::from);
    }

    @Override
    public Optional<ProductLineWithProductsResponse> selectProductLineWithOptions(Long productId) {
        return productLineJPARepository.findProductLineWithOptionsById(productId)
                .map(ProductLineWithProductsResponse::from);
    }

    @Override
    public int save(ProductLine productLine) {
       ProductLineEntity productLineEntity = convertToProductLineEntity(productLine);
       productLineJPARepository.save(productLineEntity);
       return 1;
    }

    @Override
    public int updateProductLine(ProductLine productLine) {
        ProductLineEntity productLineEntity = productLineJPARepository.findById(productLine.getProductLineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ProductLine not found with id: " + productLine.getProductLineId()));

        productLineEntity.updateProductLine(productLine);

        return 1;
    }

    @Override
    public int setDeletedAt(ProductLine productLine) {
        ProductLineEntity productLineEntity = productLineJPARepository.findById(productLine.getProductLineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ProductLine not found with id: " + productLine.getProductLineId()));

        productLineEntity.delete();
        return 1;
    }

    ProductLineEntity convertToProductLineEntity(ProductLine productLine) {
        SellerEntity seller = sellerJpaRepository.findById(productLine.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + productLine.getMemberId()));

        CategoryEntity category = categoryJpaRepository.findById(productLine.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + productLine.getCategoryId()));

        List<ProductEntity> productEntities = productJPARepository.findAllByProductId(productLine.getProductLineId());


        return ProductLineEntity.builder()
                .productLineId(productLine.getProductLineId())
                .seller(seller)
                .category(category)
                .name(productLine.getName())
                .content(productLine.getContent())
                .price(productLine.getPrice())
                .status(productLine.getStatus())
                .saleCount(productLine.getSaleCount())
                .products(productEntities)
                .build();
    }
}
