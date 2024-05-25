package org.store.clothstar.order.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.order.domain.type.PaymentMethod;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.entity.OrderEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CreateOrderRequestTest {

    @Test
    void toOrder() {
        //given
        Member member = mock(Member.class);
        Address address = mock(Address.class);
        CreateOrderRequest request = CreateOrderRequest.builder()
                .paymentMethod(PaymentMethod.CARD)
                .memberId(member.getMemberId())
                .addressId(address.getAddressId())
                .build();
        given(member.getMemberId()).willReturn(1L);
        given(address.getAddressId()).willReturn(1L);

        //when
        OrderEntity order = request.toOrder(member, address);

        //then
        assertEquals(request.getPaymentMethod(), order.getPaymentMethod());
        assertNotNull(order.getOrderId());
    }
}