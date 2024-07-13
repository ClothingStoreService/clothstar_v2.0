package org.store.clothstar.order.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.common.entity.BaseEntity;
import org.store.clothstar.order.domain.vo.Price;

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

    @Column(name = "product_line_id")
    private Long productLineId;

    @Column(name = "product_id")
    private Long productId;

    private int quantity;

    @Embedded
    Price price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public void updateDeletedAt() {
        this.deletedAt = LocalDateTime.now();
    }
}