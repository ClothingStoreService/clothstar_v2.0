package org.store.clothstar.order.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.dto.request.CreateOrderRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CreateOrderRequestTest {

    @InjectMocks
    private CreateOrderRequest request;

    @Test
    @DisplayName("toOrder: 반환값 테스트")
    void toOrder_test() {
        //given
        Member member =mock(Member.class);
        Address address =mock(Address.class);

        given(member.getMemberId()).willReturn(1L);

        //when
        Order order = request.toOrder(member, address);

        //then
        assertEquals(member.getMemberId(), order.getMemberId());
    }
}