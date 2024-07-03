package org.store.clothstar.product.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.store.clothstar.product.entity.ProductEntity;

@Getter
@Builder
public class ProductResponse {
    private Long productId;
    private Long productLineId;
    private String name;
    private int extraCharge;
    private Long stock;


    public static ProductResponse from(ProductEntity product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .productLineId(product.getProductLine().getProductLineId())
                .name(product.getName())
                .extraCharge(product.getExtraCharge())
                .stock(product.getStock())
                .build();
    }
}
