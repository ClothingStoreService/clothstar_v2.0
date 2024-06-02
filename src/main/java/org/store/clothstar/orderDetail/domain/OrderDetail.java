package org.store.clothstar.orderDetail.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetail {

    // 주문 상세 정보
    private Long orderDetailId;
    private Long orderId;
    private Long productLineId;
    private Long productId;
    private int quantity;
    private int fixedPrice; // 고정된 상품 가격 ( 주문 당시 가격 )
    private int oneKindTotalPrice; // 상품 종류 하나당 총 가격

    // 상품 정보
    private String name; // 상품명

    // 상품 옵션 정보
    private Long stock; // 옵션 상품 재고
    private String optionName;
//    private int extraCharge;

    // 판매자 정보
    private String brandName;

    public OrderDetail(OrderDetailEntity orderDetailEntity) {
        this.orderDetailId = orderDetailEntity.getOrderDetailId();
        this.orderId = orderDetailEntity.getOrder().getOrderId();
        this.productLineId = orderDetailEntity.getProductLine().getProductLineId();
        this.productId = orderDetailEntity.getProduct().getProductId();
        this.quantity = orderDetailEntity.getQuantity();
        this.fixedPrice = orderDetailEntity.getFixedPrice();
        this.oneKindTotalPrice = orderDetailEntity.getOneKindTotalPrice();
        this.name = orderDetailEntity.getProductLine().getName();
        this.stock = orderDetailEntity.getStock();
        this.optionName = orderDetailEntity.getOptionName();
        this.brandName = orderDetailEntity.getBrandName();
    }
}
