package org.store.clothstar.orderDetail.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.productLine.entity.ProductLineEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {

    private Long orderDetailId;
    private String productName; // 상품명
    private String optionName;
    private String brandName;
    private int productPrice; // 고정된 상품 가격 ( 주문 당시 가격 )
    private int extraCharge;
    private int quantity;
    private int totalPrice; // 상품 종류 하나당 총 가격

    public static OrderDetailDTO from(OrderDetailEntity orderDetailEntity, ProductEntity productEntity, ProductLineEntity productLineEntity){
        return OrderDetailDTO.builder()
                .orderDetailId(orderDetailEntity.getOrderDetailId())
                .productName(productLineEntity.getName())
                .optionName(productEntity.getName())
                .brandName(productLineEntity.getSeller().getBrandName())
                .productPrice(productLineEntity.getPrice())
                .extraCharge(productEntity.getExtraCharge())
                .quantity(orderDetailEntity.getQuantity())
                .totalPrice(orderDetailEntity.getOneKindTotalPrice())
                .build();
    }

//    @QueryProjection
//    public OrderDetailDTO(OrderDetailEntity orderDetailEntity) {
//        this.orderDetailId= orderDetailEntity.getOrderDetailId();
//        this.quantity = orderDetailEntity.getQuantity();
//        this.productPrice = orderDetailEntity.getFixedPrice();
//        this.totalPrice = orderDetailEntity.getOneKindTotalPrice();
//        this.productName = orderDetailEntity.getProductLine().getName();
//        this.optionName = orderDetailEntity.getProduct().getName();
//        this.brandName = orderDetailEntity.getProductLine().getSeller().getBrandName();
//        this.extraCharge = orderDetailEntity.getProduct().getExtraCharge();
//    }
}

