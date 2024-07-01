package org.store.clothstar.orderDetail.dto;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.productLine.entity.ProductLineEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {

    private Long orderDetailId;

    private int quantity;

    private int fixedPrice; // 고정된 상품 가격 ( 주문 당시 가격 )

    private int oneKindTotalPrice; // 상품 종류 하나당 총 가격

    // 상품 정보
    private String name; // 상품명

    // 상품 옵션 정보
    private Long stock; // 옵션 상품 재고
    private String optionName;

    // 판매자 정보
    private String brandName;

    private Long orderId;

    private int extraCharge;

    private Long productLineId;

    private Long productId;

    @QueryProjection
public OrderDetailDTO(OrderDetailEntity orderDetailEntity) {
    this.orderDetailId= orderDetailEntity.getOrderDetailId();
    this.quantity = orderDetailEntity.getQuantity();
    this.fixedPrice = orderDetailEntity.getFixedPrice();
    this.oneKindTotalPrice = orderDetailEntity.getOneKindTotalPrice();
    this.name = orderDetailEntity.getName();
    this.stock = orderDetailEntity.getStock();
    this.optionName = orderDetailEntity.getOptionName();
    this.brandName = orderDetailEntity.getBrandName();
    this.orderId = orderDetailEntity.getOrder().getOrderId();
    this.productLineId = orderDetailEntity.getProductLine().getProductLineId();
    this.productId = orderDetailEntity.getProduct().getProductId();
    this.extraCharge = orderDetailEntity.getProduct().getExtraCharge();
}
}

