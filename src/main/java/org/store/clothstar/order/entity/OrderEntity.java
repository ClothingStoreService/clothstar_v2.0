package org.store.clothstar.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.store.clothstar.order.domain.type.PaymentMethod;
import org.store.clothstar.order.domain.type.Status;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor
@Getter
@Entity(name="orders")
public class OrderEntity {
    @Id
    private Long orderId;

    @Column(name="member_id")
    private Long memberId;

    @Column(name="address_id")
    private Long addressId;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name="total_shipping_price")
    private int totalShippingPrice;

    @Column(name="total_products_price")
    private int totalProductsPrice;

    @Enumerated(EnumType.STRING)
    @Column(name="payment_method")
    private PaymentMethod paymentMethod;

    @Column(name="total_payment_price")
    private int totalPaymentPrice;
}
