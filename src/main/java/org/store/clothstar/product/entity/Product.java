package org.store.clothstar.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.product.dto.request.UpdateProductRequest;
import org.store.clothstar.productLine.entity.ProductLine;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_line_id", nullable = false)
    private ProductLine productLine;
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
}
