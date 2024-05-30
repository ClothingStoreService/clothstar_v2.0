package org.store.clothstar.orderDetail.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.orderDetail.domain.OrderDetail;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.productLine.domain.ProductLine;

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


    public OrderDetail toOrderDetail(Order order, ProductLine productLine, Product product) {
        return OrderDetail.builder()
                .orderId(order.getOrderId())
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
