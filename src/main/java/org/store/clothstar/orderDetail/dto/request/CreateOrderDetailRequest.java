package org.store.clothstar.orderDetail.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.productLine.domain.ProductLine;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "주문 상세 저장용 Request")
public class CreateOrderDetailRequest {

    @Schema(description = "상품 번호", nullable = false)
    @NotNull(message = "상품 번호는 비어있을 수 없습니다.")
    private Long productLineId;

    @Schema(description = "상품 옵션 번호", nullable = false)
    @NotNull(message = "상품 옵션 번호는 비어있을 수 없습니다.")
    private Long productId;

    @Schema(description = "상품 수량", nullable = false)
    @NotNull(message = "상품 수량은 비어있을 수 없습니다.")
    @Positive(message = "상품 수량은 0보다 커야 합니다.")
    private int quantity;


    public OrderDetailEntity toOrderDetail(long orderId, ProductLine productLine, Product product) {
        return OrderDetailEntity.builder()
                .orderId(orderId)
                .productLineId(productLine.getProductLineId())
                .productId(product.getProductId())
                .quantity(quantity)
                .fixedPrice(productLine.getPrice())
                .oneKindTotalPrice(quantity * productLine.getPrice())
                .name(productLine.getName())
                .stock(product.getStock())
                .optionName(product.getName())
//                .extraCharge(product.getExtraCharge())
                .brandName(productLine.getBrandName())
                .build();
    }
}
