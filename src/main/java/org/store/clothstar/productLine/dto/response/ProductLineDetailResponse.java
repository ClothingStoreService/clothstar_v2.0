package org.store.clothstar.productLine.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProductLineDetailResponse {
    private Long productId;
    private String name;
    private String brandName;
    private String content;
    private int price;
    private Long totalStock;
    private Long saleCount;
    private ProductLineStatus productLineStatus;
    private String biz_no;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

    public static ProductLineDetailResponse from(ProductLine productLine) {
        return ProductLineDetailResponse.builder()
                .productId(productLine.getProductLineId())
                .name(productLine.getName())
                .content(productLine.getContent())
                .brandName(productLine.getBrandName())
                .price(productLine.getPrice())
                .totalStock(productLine.getTotalStock())
                .saleCount(productLine.getSaleCount())
                .productLineStatus(productLine.getStatus())
                .biz_no(productLine.getBiz_no())
                .createdAt(productLine.getCreatedAt())
                .modifiedAt(productLine.getModifiedAt())
                .deletedAt(productLine.getDeletedAt())
                .build();
    }
}