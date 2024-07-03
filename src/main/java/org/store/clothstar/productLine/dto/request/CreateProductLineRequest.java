package org.store.clothstar.productLine.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.category.entity.CategoryEntity;
import org.store.clothstar.member.entity.SellerEntity;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.entity.ProductLineEntity;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductLineRequest {
    @Schema(description = "카테고리 아이디", nullable = false)
    @Positive(message = "카테고리 id는 0보다 큰 양수입니다.")
    private Long categoryId;

    @Schema(description = "상품 이름", nullable = false)
    @NotBlank(message = "상품 이름을 입력해주세요.")
    @Size(max = 30)
    private String name;

    @Schema(description = "상품 설명", nullable = false)
    @NotBlank(message = "상품 설명을 입력해주세요.")
    private String content;

    @Schema(description = "상품 가격", nullable = false)
    @Positive(message = "상품 가격은 0보다 커야 합니다.")
    private int price;

    @Schema(description = "상품 상태", nullable = false)
    @Builder.Default
    private ProductLineStatus status = ProductLineStatus.COMING_SOON;


    public ProductLine toProductLine(Long memberId) {
        return ProductLine.builder()
                .memberId(memberId)
                .categoryId(categoryId)
                .name(name)
                .content(content)
                .price(price)
                .totalStock(0L)
                .status(status)
                .createdAt(LocalDateTime.now())
                .saleCount(0L)
                .build();
    }

    public ProductLineEntity toProductLineEntity(SellerEntity seller, CategoryEntity category) {
        return ProductLineEntity.builder()
                .seller(seller)
                .category(category)
                .name(name)
                .content(content)
                .price(price)
//                .totalStock(0L)
                .status(status)
                .saleCount(0L)
                .build();
    }
}