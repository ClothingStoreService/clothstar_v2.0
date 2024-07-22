package org.store.clothstar.order.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.order.domain.OrderDetail;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.productLine.domain.ProductLine;

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

    public static OrderDetailDTO from(OrderDetail orderDetail, Product product, ProductLine productLine){

        return OrderDetailDTO.builder()
                .orderDetailId(orderDetail.getOrderDetailId())
                .productName(productLine.getName())
                .optionName(product.getName())
                .brandName(productLine.getSeller().getBrandName())
                .productPrice(productLine.getPrice())
                .extraCharge(product.getExtraCharge())
                .quantity(orderDetail.getQuantity())
                .totalPrice(orderDetail.getPrice().getOneKindTotalPrice())
                .build();
    }
}

