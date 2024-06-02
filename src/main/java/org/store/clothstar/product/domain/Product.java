package org.store.clothstar.product.domain;

import lombok.*;
import org.store.clothstar.product.dto.request.UpdateProductRequest;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long productId;
    private Long productLineId;
    private String name;
    private int extraCharge;
    private Long stock;

    public void updateOption(UpdateProductRequest updateProductRequest) {
        this.name = updateProductRequest.getName();
        this.extraCharge = updateProductRequest.getExtraCharge();
        this.stock = updateProductRequest.getStock();
    }

    public void updateStock(long stock) {
        this.stock = stock;
    }

    public void restoreStock(int quantity) {
        this.stock += quantity;
    }
}
