package org.store.clothstar.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.type.PaymentMethod;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.domain.vo.TotalPrice;
import org.store.clothstar.order.utils.GenerateOrderId;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주문 저장용 Request")
public class CreateOrderRequest {

    @Schema(description = "결제 수단")
    @NotNull(message = "결제 수단은 비어있을 수 없습니다.")
    private PaymentMethod paymentMethod;

    @Schema(description = "회원 번호")
    @NotNull(message = "회원 번호는 비어있을 수 없습니다.")
    private Long memberId;

    @Schema(description = "배송지 번호")
    @NotNull(message = "배송지 번호는 비어있을 수 없습니다.")
    private Long addressId;


    public Order toOrder(Member member, Address address) {
        TotalPrice totalPrice = TotalPrice.builder()
                .shipping(3000)
                .products(0)
                .payment(0)
                .build();

        return Order.builder()
                .orderId(GenerateOrderId.generateOrderId())
                .memberId(member.getMemberId())
                .addressId(address.getAddressId())
                .status(Status.WAITING)
                .paymentMethod(paymentMethod)
                .totalPrice(totalPrice)
                .build();
    }
}
