package org.store.clothstar.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.type.PaymentMethod;
import org.store.clothstar.order.type.Status;
import org.store.clothstar.order.utils.GenerateOrderId;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주문 저장용 Request")
public class CreateOrderRequest {

    @Schema(description = "결제 수단", nullable = false)
    @NotNull(message = "결제 수단은 비어있을 수 없습니다.")
    private PaymentMethod paymentMethod;

    @Schema(description = "회원 번호", nullable = false)
    @NotNull(message = "회원 번호는 비어있을 수 없습니다.")
    private Long memberId;

    @Schema(description = "배송지 번호", nullable = false)
    @NotNull(message = "배송지 번호는 비어있을 수 없습니다.")
    private Long addressId;


    public OrderEntity toOrderEntity(Member member, Address address) {
        return OrderEntity.builder()
                .orderId(GenerateOrderId.generateOrderId())
                .memberId(member.getMemberId())
                .addressId(address.getAddressId())
                .status(Status.WAITING)
                .totalShippingPrice(3000)
                .totalProductsPrice(0)
                .paymentMethod(paymentMethod)
                .totalPaymentPrice(0)
                .build();
    }
}
