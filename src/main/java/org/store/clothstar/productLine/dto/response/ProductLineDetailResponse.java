package org.store.clothstar.productLine.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import org.store.clothstar.member.dto.response.SellerSimpleResponse;
import org.store.clothstar.product.dto.response.ProductResponse;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ProductLineDetailResponse {

    @Schema(description = "상품 id", example = "1")
    private Long productLineId;

    @Schema(description = "상품 이름", example = "우유 모자")
    private String name;

    @Schema(description = "상품 이미지")
    private List<ProductImageResponse> images;

    @Schema(description = "상품 설명", example = "우유 모자입니다.")
    private String content;

    @Schema(description = "상품 가격", example = "10000")
    private int price;

    @Schema(description = "상품 상태", example = "FOR_SALE")
    private ProductLineStatus status;

    @Schema(description = "상품 옵션")
    private List<ProductOptionResponse> productList;

    @Schema(description = "옵션 조합 종류")
    private List<ItemResponse> items;

    @Schema(description = "상품 판매량", example = "10")
    private Long saleCount;  // ~개 판매중

    @Schema(description = "판매자 정보")
    private SellerSimpleResponse seller;

    @Schema(description = "생성일시")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime modifiedAt;
}
