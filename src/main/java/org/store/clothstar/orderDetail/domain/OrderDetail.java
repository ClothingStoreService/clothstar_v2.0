package org.store.clothstar.orderDetail.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.common.entity.BaseEntity;
import org.store.clothstar.order.domain.Order;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "order_detail")
public class OrderDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;

    private int quantity;

    @Column(name = "fixed_price")
    private int fixedPrice; // 고정된 상품 가격 ( 주문 당시 가격 )

    @Column(name = "onekind_total_price")
    private int oneKindTotalPrice; // 상품 종류 하나당 총 가격

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @JoinColumn(name = "product_line_id")
    private Long productLineId;

    @JoinColumn(name = "product_id")
    private Long productId;

    public void updateDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }
}