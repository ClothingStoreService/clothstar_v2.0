package org.store.clothstar.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.member.entity.AddressEntity;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.order.type.PaymentMethod;
import org.store.clothstar.order.type.Status;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name = "orders")
public class OrderEntity {
    @Id
    private Long orderId;

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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @OneToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    public void setTotalProductsPrice(int totalProductsPrice) {
        this.totalProductsPrice = totalProductsPrice;
    }

    public void setTotalPaymentPrice(int totalPaymentPrice) {
        this.totalPaymentPrice = totalPaymentPrice;
    }

        public void updatePrices(int totalProductsPrice, int totalPaymentPrice) {
        this.totalProductsPrice = totalProductsPrice;
        this.totalPaymentPrice = totalPaymentPrice;
    }

}
