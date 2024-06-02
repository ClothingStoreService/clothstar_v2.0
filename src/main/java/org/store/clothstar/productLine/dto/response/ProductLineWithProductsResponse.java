package org.store.clothstar.productLine.dto.response;

import lombok.*;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;

import java.time.LocalDateTime;
import java.util.List;

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
}
