package org.store.clothstar.order.dto.reponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.type.PaymentMethod;
import org.store.clothstar.order.type.Status;

import java.time.LocalDate;

@Getter
@Builder
@Schema(description = "주문 조회용 Response")
public class OrderResponse {

    @Schema(description = "주문 id", example = "1")
    private Long orderId;

    @Schema(description = "회원 id", example = "1")
    private Long memberId;

    @Schema(description = "배송지 id", example = "1")
    private Long addressId;

    @Schema(description = "주문 생성 날짜", example = "2024-05-15")
    private LocalDate createdAt;

    @Schema(description = "주문 상태", example = "WAITING")
    private Status status;

    @Schema(description = "총 배송비", example = "3000")
    private int totalShippingPrice;

    @Schema(description = "총 상품 금액", example = "15000")
    private int totalProductsPrice;

    @Schema(description = "결제 수단", example = "CARD")
    private PaymentMethod paymentMethod;

    @Schema(description = "총 결제 금액", example = "18000")
    private int totalPaymentPrice;


    public static OrderResponse fromOrderEntity(OrderEntity orderEntity) {
        return OrderResponse.builder()
                .orderId(orderEntity.getOrderId())
                .memberId(orderEntity.getMember().getMemberId())
                .addressId(orderEntity.getAddress().getAddressId())
                .createdAt(orderEntity.getCreatedAt().toLocalDate())
                .status(orderEntity.getStatus())
                .totalShippingPrice(orderEntity.getTotalShippingPrice())
                .totalProductsPrice(orderEntity.getTotalProductsPrice())
                .paymentMethod(orderEntity.getPaymentMethod())
                .totalPaymentPrice(orderEntity.getTotalPaymentPrice())
                .build();
    }
}
