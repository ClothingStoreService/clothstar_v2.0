package org.store.clothstar.order.entity;

import jakarta.persistence.*;
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
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
//    @Column(name="order_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

//    public static Long generateOrderId() {
//        return GenerateOrderId.generateOrderId();
//    }
//
//    public OrderEntity(){
//        this.orderId = generateOrderId();
//    }

//    @Column(name="order_detail")
//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
//    private List<OrderDetail> orderDetails;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 주문 생성일

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status; // 주문 상태

    @Column(name = "total_shipping_price")
    private int totalShippingPrice; // 총 배송비

    @Column(name = "total_products_price")
    private int totalProductsPrice; // 총 상품 금액

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // 결제 수단

    @Column(name = "total_payment_price")
    private int totalPaymentPrice; // 총 결제 금액

    public void updatePrices(int totalProductsPrice, int totalPaymentPrice) {
        this.totalProductsPrice = totalProductsPrice;
        this.totalPaymentPrice = totalPaymentPrice;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}
