package org.store.clothstar.productLine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.category.entity.Category;
import org.store.clothstar.common.entity.BaseTimeEntity;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.entity.SellerEntity;
import org.store.clothstar.product.entity.Product;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.dto.request.UpdateProductLineRequest;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductLine extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productLineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private SellerEntity seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String name;

    private String content;

    private int price;

    private Long totalStock;

    @Enumerated(EnumType.STRING)
    private ProductLineStatus status;

    private Long saleCount;

    @OneToMany(mappedBy = "productLine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

//    public void setDeletedAt() {
//        this.deletedAt = LocalDateTime.now();
//    }
}
