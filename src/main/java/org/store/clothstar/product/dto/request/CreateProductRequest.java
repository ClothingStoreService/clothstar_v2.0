package org.store.clothstar.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.productLine.entity.ProductLineEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRequest {

    @Schema(description = "상품(productLine) id", nullable = false)
    private Long productLineId;

    @Schema(description = "상품 옵션 이름", nullable = false)
    @NotBlank(message = "상품 옵션 이름을 입력해주세요.")
    private String name;

    @Schema(description = "상품 옵션 추가 금액")
    @PositiveOrZero(message = "추가 금액은 0포함 양수만 입력할 수 있습니다.")
    private int extraCharge;

    @Schema(description = "상품 옵션 재고", nullable = false)
    @PositiveOrZero(message = "0이상 양수를 입력해주세요")
    private Long stock;

    public ProductEntity toProductEntity(ProductLineEntity productLine) {
        return ProductEntity.builder()
                .productLine(productLine)
                .name(name)
                .extraCharge(extraCharge)
                .stock(stock)
                .build();
    }
}
