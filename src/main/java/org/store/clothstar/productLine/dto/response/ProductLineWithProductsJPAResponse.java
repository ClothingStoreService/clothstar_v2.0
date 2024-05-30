package org.store.clothstar.productLine.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.store.clothstar.category.dto.response.CategoryResponse;
import org.store.clothstar.category.entity.Category;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.dto.response.MemberSimpleResponse;
import org.store.clothstar.member.dto.response.SellerSimpleResponse;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.product.entity.Product;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.entity.ProductLine;

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
    private List<Product> productList;
    private Long saleCount;  // ~개 판매중
    private MemberSimpleResponse member;
    private SellerSimpleResponse seller;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
//    private LocalDateTime deletedAt;

    @QueryProjection
    public ProductLineWithProductsResponse(ProductLine productLine, Category category, Seller seller, MemberEntity member) {
        this.productLineId = productLine.getProductLineId();
        this.category = CategoryResponse.from(category);
        this.name = productLine.getName();
        this.price = productLine.getPrice();
        this.totalStock = productLine.getTotalStock();
        this.status = productLine.getStatus();
        this.productList = productLine.getProducts();
        this.saleCount = productLine.getSaleCount();
        this.member = MemberSimpleResponse.from(member);
        this.seller = SellerSimpleResponse.from(seller);
        this.createdAt = productLine.getCreatedAt();
        this.modifiedAt = productLine.getModifiedAt();
//        this.deletedAt = productLine.getDeletedAt();
    }
}
