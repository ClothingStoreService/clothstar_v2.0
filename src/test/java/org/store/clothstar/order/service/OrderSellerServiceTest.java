package org.store.clothstar.order.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.member.entity.AddressEntity;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.order.OrderRepository;
import org.store.clothstar.order.repository.orderSeller.OrderSellerRepository;
import org.store.clothstar.order.type.Status;
import org.store.clothstar.orderDetail.service.OrderDetailService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@Nested
@ExtendWith(MockitoExtension.class)
class OrderSellerServiceTest {

    @InjectMocks
    private OrderSellerService orderSellerService;

    @Mock
    private OrderEntity mockOrderEntity;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderSellerRepository orderSellerRepository;

    @Mock
    private OrderDetailService orderDetailService;

    @Mock
    private MemberEntity mockMemberEntity;

    @Mock
    private AddressEntity mockAddressEntity;

    @Test
    @DisplayName("getWaitingOrders: '승인대기' 주문 조회 - 메서드 호출 & 반환값 테스트")
    void getWaitingOrder_test() {
        //given
        OrderEntity order1 = mock(OrderEntity.class);
//        given(order1.getCreatedAt()).willReturn(LocalDateTime.now());
        given(order1.getTotalShippingPrice()).willReturn(1000);
//        given(order1.getMember()).willReturn(mockMemberEntity);
//        given(order1.getAddress()).willReturn(mockAddressEntity);

        OrderEntity order2 = mock(OrderEntity.class);
//        given(order2.getCreatedAt()).willReturn(LocalDateTime.now());
//        given(order2.getMember()).willReturn(mockMemberEntity);
//        given(order2.getAddress()).willReturn(mockAddressEntity);

        OrderEntity order3 = mock(OrderEntity.class);
//        given(order3.getCreatedAt()).willReturn(LocalDateTime.now());
//        given(order3.getMember()).willReturn(mockMemberEntity);
//        given(order3.getAddress()).willReturn(mockAddressEntity);

        List<OrderEntity> orders = List.of(order1, order2, order3);
        given(orderSellerRepository.findWaitingOrders()).willReturn(orders);

        //when
        List<OrderEntity> response = orderSellerService.getWaitingOrder();

        //then
        then(orderSellerRepository).should(times(1)).findWaitingOrders();
        assertThat(response).isNotNull().hasSize(3);
        assertThat(response.get(0).getTotalShippingPrice()).isEqualTo(1000);
    }

    @Test
    @DisplayName("approveOrder: 판매자 주문 승인 - 메서드 호출 & 반환값 테스트")
    void approveOrder_verify_test() {
        //given
        Long orderId = 1L;
        given(mockOrderEntity.getStatus()).willReturn(Status.WAITING);
        given(mockOrderEntity.getCreatedAt()).willReturn(LocalDateTime.now());
        given(orderRepository.findById(orderId)).willReturn(Optional.of(mockOrderEntity));
        given(mockOrderEntity.getMember()).willReturn(mockMemberEntity);
        given(mockOrderEntity.getAddress()).willReturn(mockAddressEntity);

        //when
        MessageDTO messageDTO = orderSellerService.approveOrder(orderId);

        //then
        then(orderSellerRepository).should(times(1)).approveOrder(orderId);
        then(orderRepository).should(times(2)).findById(orderId);
        assertThat(messageDTO.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(messageDTO.getMessage()).isEqualTo("주문이 정상적으로 승인 되었습니다.");
    }

    @Test
    @DisplayName("cancelOrder: 판매자 주문 취소 - 메서드 호출 & 반환값 테스트")
    void cancelOrder_verify_test() {
        // given
        Long orderId = 1L;
        given(mockOrderEntity.getStatus()).willReturn(Status.WAITING);
        given(mockOrderEntity.getCreatedAt()).willReturn(LocalDateTime.now());
        given(orderRepository.findById(orderId)).willReturn(Optional.of(mockOrderEntity));
        given(mockOrderEntity.getMember()).willReturn(mockMemberEntity);
        given(mockOrderEntity.getAddress()).willReturn(mockAddressEntity);

        //when
        MessageDTO messageDTO = orderSellerService.cancelOrder(orderId);

        //then
        then(orderSellerRepository).should(times(1)).cancelOrder(orderId);
        then(orderRepository).should(times(2)).findById(orderId);
        assertThat(messageDTO.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(messageDTO.getMessage()).isEqualTo("주문이 정상적으로 취소 되었습니다.");
    }

    @Test
    @DisplayName("approveOrder: 주문상태가 '승인대기'가 아닐 때 예외처리 테스트")
    void approveOrder_NotWAITING_exception_test() {

        //given
        Long orderId = 1L;
        given(orderRepository.findById(orderId)).willReturn(Optional.of(mockOrderEntity));
        given(mockOrderEntity.getStatus()).willReturn(Status.DELIVERED);

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            orderSellerService.approveOrder(orderId);
        });

        //then
        assertEquals("400 BAD_REQUEST \"주문이 존재하지 않거나 상태가 'WAITING'이 아니어서 처리할 수 없습니다.\"", thrown.getMessage());
    }

    @Test
    @DisplayName("cancelOrder: 주문상태가 '승인대기'가 아닐 때 예외처리 테스트")
    void cancelOrder_NotWAITING_exception_test() {

        //given
        Long orderId = 1L;
        given(orderRepository.findById(orderId)).willReturn(Optional.of(mockOrderEntity));
        given(mockOrderEntity.getStatus()).willReturn(Status.DELIVERED);

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            orderSellerService.cancelOrder(orderId);
        });

        //then
        assertEquals("400 BAD_REQUEST \"주문이 존재하지 않거나 상태가 'WAITING'이 아니어서 처리할 수 없습니다.\"", thrown.getMessage());
    }
}