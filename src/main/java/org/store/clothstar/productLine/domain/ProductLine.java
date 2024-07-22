package org.store.clothstar.productLine.domain;

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
import org.store.clothstar.product.domain.Product;
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
//@Table(name = "product_line")
public class ProductLine extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productLineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    private String name;

    private String content;

    private int price;

//    private Long totalStock;

    @Enumerated(EnumType.STRING)
    private ProductLineStatus status;

    private Long saleCount;

    @OneToMany(mappedBy = "productLine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonBackReference
    @JsonIgnore
    private List<Product> products;

    public void updateProductLine(UpdateProductLineRequest updateProductLineRequest) {
        this.name = updateProductLineRequest.getName();
        this.content = updateProductLineRequest.getContent();
        this.price = updateProductLineRequest.getPrice();
        this.status = updateProductLineRequest.getStatus();
    }

    public void changeProductStatus(ProductLineStatus productLineStatus) {
        this.status = productLineStatus;
    }

    // 상품 전체 재고 계산
    public long calculateTotalStock() {
        return products.stream().mapToLong(Product::getStock).sum();
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

    public void addProduct(Product product) {
        if (products == null) {
            products = new ArrayList<>();
        }
        products.add(product);
        product.setterProductLine(this);
    }
}
