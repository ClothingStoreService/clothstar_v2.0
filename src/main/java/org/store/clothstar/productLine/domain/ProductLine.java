package org.store.clothstar.productLine.domain;

import lombok.*;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.dto.request.UpdateProductLineRequest;

import java.time.LocalDateTime;

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
}