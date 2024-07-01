package org.store.clothstar.orderDetail.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {

    private Long orderDetailId;
    private String ProductName; // 상품명
    private String optionName;
    private String brandName;
    private int productPrice; // 고정된 상품 가격 ( 주문 당시 가격 )
    private int extraCharge;
    private int quantity;
    private int totalPrice; // 상품 종류 하나당 총 가격

    @QueryProjection
    public OrderDetailDTO(OrderDetailEntity orderDetailEntity) {
        this.orderDetailId= orderDetailEntity.getOrderDetailId();
        this.quantity = orderDetailEntity.getQuantity();
        this.productPrice = orderDetailEntity.getFixedPrice();
        this.totalPrice = orderDetailEntity.getOneKindTotalPrice();
        this.ProductName = orderDetailEntity.getName();
        this.optionName = orderDetailEntity.getOptionName();
        this.brandName = orderDetailEntity.getBrandName();
        this.extraCharge = orderDetailEntity.getProduct().getExtraCharge();
    }
}

