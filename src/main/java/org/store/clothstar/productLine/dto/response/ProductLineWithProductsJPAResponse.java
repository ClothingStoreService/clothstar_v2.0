package org.store.clothstar.productLine.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.store.clothstar.category.dto.response.CategoryResponse;
import org.store.clothstar.category.entity.CategoryEntity;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.dto.response.MemberSimpleResponse;
import org.store.clothstar.member.dto.response.SellerSimpleResponse;
import org.store.clothstar.product.dto.response.ProductResponse;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.entity.ProductLineEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductLineWithProductsJPAResponse {

    private Long productLineId;
    private String name;
    private String content;
    private int price;
    private Long totalStock;
    private ProductLineStatus status;
    private List<ProductResponse> productList;
    private Long saleCount;  // ~개 판매중
    private SellerSimpleResponse seller;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @QueryProjection
    public ProductLineWithProductsJPAResponse(ProductLineEntity productLine, SellerEntity seller, Long totalStock) {
        this.productLineId = productLine.getProductLineId();
        this.name = productLine.getName();
        this.content = productLine.getContent();
        this.price = productLine.getPrice();
        this.totalStock = totalStock;
        this.status = productLine.getStatus();
        this.saleCount = productLine.getSaleCount();
        this.seller = SellerSimpleResponse.from(seller);
        this.createdAt = productLine.getCreatedAt();
        this.modifiedAt = productLine.getModifiedAt();
        this.productList = new ArrayList<>();
    }

    public void setProductList(List<ProductResponse> productList) {
        this.productList = productList != null ? productList : new ArrayList<>();
    }

}
