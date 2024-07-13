package org.store.clothstar.orderDetail.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.productLine.entity.ProductLineEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "주문 상세 추가용 Request")
public class AddOrderDetailRequest {

    @Schema(description = "주문 번호", nullable = false)
    @NotNull(message = "주문 번호는 비어있을 수 없습니다.")
    private Long orderId;

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


    public OrderDetailEntity toOrderDetailEntity(OrderEntity orderEntity, ProductLineEntity productLineEntity, ProductEntity productEntity) {
        return OrderDetailEntity.builder()
                .order(orderEntity)
                .productLineId(productLineEntity.getProductLineId())
                .productId(productEntity.getProductId())
                .quantity(quantity)
                .fixedPrice(productLineEntity.getPrice())
                .oneKindTotalPrice(quantity * productLineEntity.getPrice())
                .build();
    }
}
