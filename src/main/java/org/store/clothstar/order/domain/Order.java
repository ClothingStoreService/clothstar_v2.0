package org.store.clothstar.order.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.order.domain.type.PaymentMethod;
import org.store.clothstar.order.domain.type.Status;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    private Long orderId;
    private Long memberId;
    private Long addressId;
    private LocalDateTime createdAt; // 주문 생성일
    private Status status; // 주문 상태
    private int totalShippingPrice; // 총 배송비
    private int totalProductsPrice; // 총 상품 금액
    private PaymentMethod paymentMethod; // 결제 수단
    private int totalPaymentPrice; // 총 결제 금액

    public void updatePrices(int totalProductsPrice, int totalPaymentPrice) {
        this.totalProductsPrice = totalProductsPrice;
        this.totalPaymentPrice = totalPaymentPrice;
    }
}
