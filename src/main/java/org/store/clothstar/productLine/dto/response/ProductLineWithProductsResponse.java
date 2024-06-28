package org.store.clothstar.productLine.dto.response;

import lombok.*;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.product.dto.response.ProductResponse;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductLineWithProductsResponse {
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
    private List<Product> productList;

    public static ProductLineWithProductsResponse from(ProductLineWithProductsJPAResponse response) {
        return ProductLineWithProductsResponse.builder()
                .productLineId(response.getProductLineId())
                .memberId(response.getMember().getMemberId())
                .categoryId(response.getCategory().getCategoryId())
                .name(response.getName())
                .content(response.getContent())
                .price(response.getPrice())
                .totalStock(response.getTotalStock())
                .status(response.getStatus())
                .saleCount(response.getSaleCount())
                .createdAt(response.getCreatedAt())
                .modifiedAt(response.getModifiedAt())
                .brandName(response.getSeller().getBrandName())
                .biz_no(response.getSeller().getBizNo())
                .productList(convertToProductList(response.getProductList()))
                .build();
    }

    private static List<Product> convertToProductList(List<ProductResponse> productResponses) {
        if (productResponses == null) {
            return Collections.emptyList();
        }
        return productResponses.stream()
                .map(pr -> Product.builder()
                        .productId(pr.getProductId())
                        .productLineId(pr.getProductLineId())
                        .name(pr.getName())
                        .extraCharge(pr.getExtraCharge())
                        .stock(pr.getStock())
                        .build())
                .collect(Collectors.toList());
    }
}
