package org.store.clothstar.productLine.domain;

import lombok.*;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.dto.request.UpdateProductLineRequest;
import org.store.clothstar.productLine.entity.ProductLineEntity;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductLine {
    private Long productLineId;
    private Long memberId;
    private Long categoryId;
    private String name;
    private String content;
    private int price;
    private Long totalStock;
    private ProductLineStatus status;
    private Long saleCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private String brandName;
    private String biz_no;

    public void updateProductLine(UpdateProductLineRequest updateProductLineRequest) {
        this.name = updateProductLineRequest.getName();
        this.content = updateProductLineRequest.getContent();
        this.price = updateProductLineRequest.getPrice();
        this.status = updateProductLineRequest.getStatus();
        this.modifiedAt = LocalDateTime.now();
    }

    public void changeProductStatus(ProductLineStatus productLineStatus) {
        this.status = productLineStatus;
    }

    public void setDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }

    public static ProductLine from(ProductLineEntity productLine) {
        List<ProductEntity> products = productLine.getProducts();

        Long totalStock = 0L;
        for (ProductEntity product : products) {
            totalStock += product.getStock();
        }

        return ProductLine.builder()
                .productLineId(productLine.getProductLineId())
                .memberId(productLine.getSeller().getMemberId())
                .categoryId(productLine.getCategory().getCategoryId())
                .name(productLine.getName())
                .content(productLine.getContent())
                .price(productLine.getPrice())
                .totalStock(totalStock)
                .status(productLine.getStatus())
                .saleCount(productLine.getSaleCount())
                .createdAt(productLine.getCreatedAt())
                .deletedAt(productLine.getDeletedAt())
                .brandName(productLine.getSeller().getBrandName())
                .biz_no(productLine.getSeller().getBizNo())
                .build();
    }
}