package org.store.clothstar.order.dto.reponse;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.member.entity.AddressEntity;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.type.PaymentMethod;
import org.store.clothstar.order.type.Status;
import org.store.clothstar.orderDetail.dto.OrderDetailDTO;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;
import org.store.clothstar.productLine.entity.ProductLineEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "주문 페이지 조회용 Response")
public class OrderPageResponse {
    @Schema(description = "주문 id", example = "1")
    private Long orderId;

    @Schema(description = "주문자 이름", example = "수빈")
    private String ordererName;

    @Schema(description = "주문 생성 날짜", example = "2024-05-15")
    private LocalDate createdAt;

    @Schema(description = "주문 상태", example = "WAITING")
    private Status status;

    //address
    private String receiverName;
    private String addressBasic;
    private String addressDetail;
    private String telNo;
    private String deliveryRequest;

    @Schema(description = "결제 수단", example = "CARD")
    private PaymentMethod paymentMethod;

    @Schema(description = "총 배송비", example = "3000")
    private int totalShippingPrice;

    @Schema(description = "총 상품 금액", example = "15000")
    private int totalProductsPrice;

    @Schema(description = "총 결제 금액", example = "18000")
    private int totalPaymentPrice;

    private List<OrderDetailEntity> orderDetails;

    public static OrderPageResponse from(OrderEntity orderEntity) {
        return OrderPageResponse.builder()
                .orderId(orderEntity.getOrderId())
                .ordererName(orderEntity.getMember().getName())
                .createdAt(orderEntity.getCreatedAt().toLocalDate())
                .status(orderEntity.getStatus())
                .totalShippingPrice(orderEntity.getTotalShippingPrice())
                .totalProductsPrice(orderEntity.getTotalProductsPrice())
                .paymentMethod(orderEntity.getPaymentMethod())
                .totalPaymentPrice(orderEntity.getTotalPaymentPrice())
                .telNo(orderEntity.getAddress().getTelNo())
                .deliveryRequest(orderEntity.getAddress().getDeliveryRequest())
                .receiverName(orderEntity.getAddress().getReceiverName())
                .addressBasic(orderEntity.getAddress().getAddressBasic())
                .addressDetail(orderEntity.getAddress().getAddressDetail())
                .build();
    }
}
