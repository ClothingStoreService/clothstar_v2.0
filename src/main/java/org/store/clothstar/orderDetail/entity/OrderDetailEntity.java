package org.store.clothstar.orderDetail.entity;

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
@Entity(name="order_detail")
//@Table(name="order_detail")
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;

    @Column(name="order_id")
    private Long orderId;

    @Column(name="product_line_id")
    private Long productLineId;

    @Column(name="product_id")
    private Long productId;

    @Column(name="quantity")
    private int quantity;

    @Column(name="fixed_price")
    private int fixedPrice; // 고정된 상품 가격 ( 주문 당시 가격 )

    @Column(name="onekind_total_price")
    private int oneKindTotalPrice; // 상품 종류 하나당 총 가격

    // 상품 정보
    @Column(name="name")
    private String name; // 상품명

    // 상품 옵션 정보
    @Column(name="stock")
    private Long stock; // 옵션 상품 재고
    @Column(name="option_name")
    private String optionName;
//    @Column(name="order_id")
//    private int extraCharge;

    // 판매자 정보
    @Column(name="brand_name")
    private String brandName;


//    @Enumerated(EnumType.STRING)

}

