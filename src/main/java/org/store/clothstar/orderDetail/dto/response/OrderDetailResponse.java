package org.store.clothstar.orderDetail.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.store.clothstar.orderDetail.domain.OrderDetail;

@Getter
@Builder
@Schema(description = "주문 상세 조회용 Response")
public class OrderDetailResponse {

    @Schema(description = "주문 상세 번호", example = "1")
    private Long orderDetailId;

    @Schema(description = "주문 번호", example = "1")
    private Long orderId;

    @Schema(description = "상품 번호", example = "1")
    private Long productLineId;

    @Schema(description = "상품 옵션 번호", example = "1")
    private Long productId;

    @Schema(description = "상품 수량", example = "2")
    private int quantity;

    @Schema(description = "고정 가격", example = "15000")
    private int fixedPrice;

    @Schema(description = "상품 종류 하나당 총 가격", example = "30000")
    private int oneKindTotalPrice;

    @Schema(description = "상품 이름", example = "나이키 반팔티")
    private String name;

    @Schema(description = "옵션 상품 재고", example = "30")
    private Long stock;

    @Schema(description = "옵션 이름", example = "검정")
    private String optionName;

    @Schema(description = "옵션 추가 비용", example = "0")
    private int extraCharge;

    @Schema(description = "브랜드 이름", example = "나이키")
    private String brandName;


    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail) {
        return OrderDetailResponse.builder()
                .orderDetailId(orderDetail.getOrderDetailId())
                .orderId(orderDetail.getOrderId())
                .productLineId(orderDetail.getProductLineId())
                .productId(orderDetail.getProductId())
                .quantity(orderDetail.getQuantity())
                .fixedPrice(orderDetail.getFixedPrice())
                .oneKindTotalPrice(orderDetail.getOneKindTotalPrice())
                .name(orderDetail.getName())
                .stock(orderDetail.getStock())
                .optionName(orderDetail.getOptionName())
                .extraCharge(orderDetail.getExtraCharge())
                .brandName(orderDetail.getBrandName())
                .build();
    }
}
