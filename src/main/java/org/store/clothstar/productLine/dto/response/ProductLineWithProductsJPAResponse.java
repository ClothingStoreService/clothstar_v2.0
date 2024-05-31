package org.store.clothstar.productLine.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.store.clothstar.category.domain.Category;
import org.store.clothstar.category.dto.response.CategoryResponse;
import org.store.clothstar.member.dto.response.MemberSimpleResponse;
import org.store.clothstar.member.dto.response.SellerSimpleResponse;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.entity.SellerEntity;
import org.store.clothstar.product.dto.response.ProductResponse;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.entity.ProductLineEntity;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductLineWithProductsJPAResponse {

    private Long productLineId;
    private CategoryResponse category;
    private String name;
    private String content;
    private int price;
    private Long totalStock;
    private ProductLineStatus status;
//    private List<ProductEntity> productList;
    private List<ProductResponse> productList;
    private Long saleCount;  // ~개 판매중
    private MemberSimpleResponse member;
    private SellerSimpleResponse seller;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
//    private LocalDateTime deletedAt;

    @QueryProjection
    public ProductLineWithProductsJPAResponse(ProductLineEntity productLine, Category category, SellerEntity seller, MemberEntity member, Long totalStock) {
        this.productLineId = productLine.getProductLineId();
        this.category = CategoryResponse.from(category);
        this.name = productLine.getName();
        this.price = productLine.getPrice();
        this.totalStock = totalStock;
        this.status = productLine.getStatus();
        this.productList = productLine.getProducts()
                .stream()
                .map(ProductResponse::from)
                .toList();
//        this.productList = productLine.getProducts();
        this.saleCount = productLine.getSaleCount();
        this.member = MemberSimpleResponse.from(member);
        this.seller = SellerSimpleResponse.from(seller);
        this.createdAt = productLine.getCreatedAt();
        this.modifiedAt = productLine.getModifiedAt();
//        this.deletedAt = productLine.getDeletedAt();
    }
}
