package org.store.clothstar.order.dto.reponse;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
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
@Schema(description = "주문 조회용 Response")
public class OrderResponse {

    @Schema(description = "주문 id", example = "1")
    private Long orderId;

    @Schema(description = "주문자 이름", example = "수빈")
    private String ordererName;

    @Schema(description = "주문 생성 날짜", example = "2024-05-15")
    private LocalDate createdAt;

    @Schema(description = "주문 상태", example = "WAITING")
    private Status status;

    @Schema(description = "주소 정보")
    private AddressDTO address;

    @Schema(description = "결제 수단", example = "CARD")
    private PaymentMethod paymentMethod;

    @Schema(description = "총 배송비", example = "3000")
    private int totalShippingPrice;

    @Schema(description = "총 상품 금액", example = "15000")
    private int totalProductsPrice;

    @Schema(description = "총 결제 금액", example = "18000")
    private int totalPaymentPrice;

    @Builder.Default
    private List<OrderDetailDTO> orderDetailList = new ArrayList<>();

    @QueryProjection
    public OrderResponse(OrderEntity orderEntity,
                         OrderDetailEntity orderDetailEntity,
                         Member member, Address address,
                         ProductLineEntity productLineEntity) {
        this.orderId = orderEntity.getOrderId();
        this.ordererName = member.getName();
        this.createdAt = orderEntity.getCreatedAt().toLocalDate();
        this.status = orderEntity.getStatus();
        this.totalShippingPrice = orderEntity.getTotalShippingPrice();
        this.totalProductsPrice = orderEntity.getTotalProductsPrice();
        this.paymentMethod = orderEntity.getPaymentMethod();
        this.totalPaymentPrice = orderEntity.getTotalPaymentPrice();
        this.address = AddressDTO.builder()
                .receiverName(address.getReceiverName())
                .addressBasic(address.getAddressInfo().getAddressBasic())
                .addressDetail(address.getAddressInfo().getAddressDetail())
                .telNo(address.getTelNo())
                .deliveryRequest(address.getAddressInfo().getDeliveryRequest())
                .build();
        this.orderDetailList = new ArrayList<>();
    }

    public static OrderResponse from(OrderEntity orderEntity, Member member, Address address) {
        return OrderResponse.builder()
                .orderId(orderEntity.getOrderId())
                .ordererName(member.getName())
                .createdAt(orderEntity.getCreatedAt().toLocalDate())
                .status(orderEntity.getStatus())
                .totalShippingPrice(orderEntity.getTotalShippingPrice())
                .totalProductsPrice(orderEntity.getTotalProductsPrice())
                .paymentMethod(orderEntity.getPaymentMethod())
                .totalPaymentPrice(orderEntity.getTotalPaymentPrice())
                .address(AddressDTO.builder()
                        .receiverName(address.getReceiverName())
                        .addressBasic(address.getAddressInfo().getAddressBasic())
                        .addressDetail(address.getAddressInfo().getAddressDetail())
                        .telNo(address.getTelNo())
                        .deliveryRequest(address.getAddressInfo().getDeliveryRequest())
                        .build())
                .build();
    }

    public void setterOrderDetailList(List<OrderDetailDTO> orderDetailDTOList) {
        this.orderDetailList = orderDetailDTOList;
    }
}
