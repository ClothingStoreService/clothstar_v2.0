package org.store.clothstar.order.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.store.clothstar.member.entity.AddressEntity;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.entity.OrderEntity;

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
        MemberEntity member = mock(MemberEntity.class);
        AddressEntity address = mock(AddressEntity.class);

        given(member.getMemberId()).willReturn(1L);

        //when
        OrderEntity orderEntity = request.toOrderEntity(member, address);

        //then
        assertEquals(member.getMemberId(), orderEntity.getMember().getMemberId());
    }
}