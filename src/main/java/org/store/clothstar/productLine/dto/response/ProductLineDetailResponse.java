package org.store.clothstar.productLine.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.store.clothstar.member.dto.response.SellerSimpleResponse;
import org.store.clothstar.product.dto.response.ProductResponse;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.entity.ProductLineEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductLineDetailResponse {

    @Schema(description = "상품 id", example = "1")
    private Long productLineId;

    @Schema(description = "상품 이름", example = "우유 모자")
    private String name;

    @Schema(description = "상품 설명", example = "우유 모자입니다.")
    private String content;

    @Schema(description = "상품 가격", example = "10000")
    private int price;

    @Schema(description = "상품 전체 재고", example = "100")
    private Long totalStock;

    @Schema(description = "상품 상태", example = "FOR_SALE")
    private ProductLineStatus status;

    @Schema(description = "상품 판매량", example = "10")
    private Long saleCount;  // ~개 판매중

    @Schema(description = "상품 옵션")
    private List<ProductResponse> productList;

    @Schema(description = "판매자 정보")
    private SellerSimpleResponse seller;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime modifiedAt;

    public ProductLineDetailResponse(ProductLineEntity productLine) {
        this.productLineId = productLine.getProductLineId();
        this.name = productLine.getName();
        this.content = productLine.getContent();
        this.price = productLine.getPrice();
        this.totalStock = productLine.getProducts().stream().mapToLong(ProductEntity::getStock).sum();
        this.status = productLine.getStatus();
        this.saleCount = productLine.getSaleCount();
        this.createdAt = productLine.getCreatedAt();
        this.modifiedAt = productLine.getModifiedAt();
        this.seller = SellerSimpleResponse.from(productLine.getSeller());
        this.productList = productLine.getProducts().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
