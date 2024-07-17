package org.store.clothstar.productLine.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;

@Getter
@Builder
@Slf4j
public class ProductLineResponse {
    private Long productLineId;
    private String brandName;
    private String name;
    private String content;
    private int price;
    //    private Long totalStock;
    private ProductLineStatus productLineStatus;

    public static ProductLineResponse from(ProductLine productLine) {
        return ProductLineResponse.builder()
                .productLineId(productLine.getProductLineId())
                .brandName(productLine.getSeller().getBrandName())
                .name(productLine.getName())
                .content(productLine.getContent())
                .price(productLine.getPrice())
//                .totalStock(productLine.getTotalStock())
                .productLineStatus(productLine.getStatus())
                .build();
    }
}
