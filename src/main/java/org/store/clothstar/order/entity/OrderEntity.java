package org.store.clothstar.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.order.domain.type.PaymentMethod;
import org.store.clothstar.order.domain.type.Status;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name = "orders")
public class OrderEntity {
    @Id
    private Long orderId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "address_id")
    private Long addressId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "total_shipping_price")
    private int totalShippingPrice;

    @Column(name = "total_products_price")
    private int totalProductsPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "total_payment_price")
    private int totalPaymentPrice;

    public void setTotalProductsPrice(int totalProductsPrice) {
        this.totalProductsPrice = totalProductsPrice;
    }

    public void setTotalPaymentPrice(int totalPaymentPrice) {
        this.totalPaymentPrice = totalPaymentPrice;
    }

}
