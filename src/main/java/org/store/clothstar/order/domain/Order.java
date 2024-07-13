package org.store.clothstar.order.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.common.entity.BaseEntity;
import org.store.clothstar.order.domain.type.PaymentMethod;
import org.store.clothstar.order.domain.type.Status;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name = "orders")
public class Order extends BaseEntity {
    @Id
    private Long orderId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

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

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "address_id")
    private Long addressId;

    public void updatePrices(int totalProductsPrice, int totalPaymentPrice) {
        this.totalProductsPrice = totalProductsPrice;
        this.totalPaymentPrice = totalPaymentPrice;
    }

    public void updateDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }
}
