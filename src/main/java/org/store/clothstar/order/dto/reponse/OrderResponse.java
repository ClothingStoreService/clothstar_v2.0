package org.store.clothstar.order.dto.reponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.type.PaymentMethod;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.domain.vo.AddressDTO;
import org.store.clothstar.order.domain.vo.OrderDetailDTO;
import org.store.clothstar.order.domain.vo.TotalPrice;

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

    private TotalPrice totalPrice;

    @Builder.Default
    private List<OrderDetailDTO> orderDetailList = new ArrayList<>();

    public static OrderResponse from(Order order, Member member, Address address) {
        TotalPrice totalPrice = TotalPrice.builder()
                .shipping(order.getTotalPrice().getShipping())
                .products(order.getTotalPrice().getProducts())
                .payment(order.getTotalPrice().getPayment())
                .build();

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .ordererName(member.getName())
                .createdAt(order.getCreatedAt().toLocalDate())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .totalPrice(totalPrice)
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
