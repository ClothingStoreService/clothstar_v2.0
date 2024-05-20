package org.store.clothstar.productLine.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductLineRequest {
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

}
