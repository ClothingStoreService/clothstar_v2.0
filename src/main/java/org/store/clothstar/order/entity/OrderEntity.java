package org.store.clothstar.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.common.entity.BaseEntity;
import org.store.clothstar.member.entity.AddressEntity;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.order.type.PaymentMethod;
import org.store.clothstar.order.type.Status;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name = "orders")
public class OrderEntity extends BaseEntity {
    @Id
    private Long orderId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailEntity> orderDetails;

//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;

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

    public void updatePrices(int totalProductsPrice, int totalPaymentPrice) {
        this.totalProductsPrice = totalProductsPrice;
        this.totalPaymentPrice = totalPaymentPrice;
    }

    public void updateDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }
}
