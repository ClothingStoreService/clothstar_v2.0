package org.store.clothstar.orderDetail.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="order_detail")
public class OrderDetailEntity {
    // 주문 상세 정보
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_detail_id")
    private Long orderDetailId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name="product_line_id",nullable = false)
    private Long productLineId;

    @Column(name="product_id",nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @Column(name="fixed_price",nullable = false)
    private int fixedPrice; // 고정된 상품 가격 ( 주문 당시 가격 )

    @Column(name="onekind_total_price",nullable = false)
    private int oneKindTotalPrice; // 상품 종류 하나당 총 가격

    // 상품 정보
    @Column(nullable = false)
    private String name; // 상품명

    // 상품 옵션 정보
    @Column(name="stock",nullable = false)
    private Long stock; // 옵션 상품 재고

    @Column(name="option_name",nullable = false)
    private String optionName;

//    @Column(name="order_id",nullable = false)
//    private int extraCharge;

    // 판매자 정보
    @Column(name="brand_name",nullable = false)
    private String brandName;
}
