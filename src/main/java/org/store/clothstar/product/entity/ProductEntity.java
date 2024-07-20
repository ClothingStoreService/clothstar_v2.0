package org.store.clothstar.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.product.dto.request.UpdateProductRequest;
import org.store.clothstar.productLine.entity.ProductLineEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@BatchSize(size = 100)
@Entity(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String name;
    private int extraCharge;
    private Long stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_line_id", nullable = false)
    private ProductLineEntity productLine;

    public void updateOption(UpdateProductRequest updateProductRequest) {
        this.name = updateProductRequest.getName();
        this.extraCharge = updateProductRequest.getExtraCharge();
        this.stock = updateProductRequest.getStock();
    }

    public void updateOption(Product pro) {
        this.name = pro.getName();
        this.extraCharge = pro.getExtraCharge();
        this.stock = pro.getStock();
    }

    public void updateStock(long stock) {
        this.stock = stock;
        checkAndUpdateProductLineStatus();
    }

    //  재고를 차감하고 상태를 변경하는 메서드
    public void reduceStock(int quantity) {
        if (this.stock >= quantity) {
            this.stock -= quantity;
            checkAndUpdateProductLineStatus();
        } else {
            throw new IllegalArgumentException("Insufficient stock");
        }
    }

    // 재고 변경 시 ProductLine 상태 업데이트 메서드
    private void checkAndUpdateProductLineStatus() {
        if (productLine != null) {
            productLine.checkAndUpdateStatus();
        }
    }

    public void restoreStock(int quantity) {
        this.stock += quantity;
    }

    public Long getId() {
        return productId;
    }

    public void setterProductLine(ProductLineEntity productLineEntity) {
        this.productLine = productLineEntity;
    }
}
