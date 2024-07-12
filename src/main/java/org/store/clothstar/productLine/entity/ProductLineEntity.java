package org.store.clothstar.productLine.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.store.clothstar.category.entity.CategoryEntity;
import org.store.clothstar.common.entity.BaseTimeEntity;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.dto.request.UpdateProductLineRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@BatchSize(size = 100)
@Entity(name = "product_line")
public class ProductLineEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productLineId;
    private String name;
    private String content;
    private int price;
    private Long saleCount;

    @Enumerated(EnumType.STRING)
    private ProductLineStatus status;


    @OneToMany(mappedBy = "productLine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProductEntity> products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Seller seller;

    public void updateProductLine(UpdateProductLineRequest updateProductLineRequest) {
        this.name = updateProductLineRequest.getName();
        this.content = updateProductLineRequest.getContent();
        this.price = updateProductLineRequest.getPrice();
        this.status = updateProductLineRequest.getStatus();
    }

    public void updateProductLine(ProductLine productLine) {
        this.name = productLine.getName();
        this.content = productLine.getContent();
        this.price = productLine.getPrice();
        this.status = productLine.getStatus();
        this.setModifiedAt(LocalDateTime.now());
    }

    public void changeProductStatus(ProductLineStatus productLineStatus) {
        this.status = productLineStatus;
    }

    // 상품 전체 재고 계산
    public long calculateTotalStock() {
        return products.stream().mapToLong(ProductEntity::getStock).sum();
    }

    // 상품 전체 재고 확인 및 상태 업데이트
    public void checkAndUpdateStatus() {
        long totalStock = calculateTotalStock();
        if (totalStock == 0 && this.status != ProductLineStatus.SOLD_OUT) {
            this.status = ProductLineStatus.SOLD_OUT;
        }
    }

    public void delete() {
        this.setDeletedAt(LocalDateTime.now());
    }

    public Long getId() {
        return productLineId;
    }

    public void addProduct(ProductEntity product) {
        if (products == null) {
            products = new ArrayList<>();
        }
        products.add(product);
        product.setterProductLine(this);
    }
}
