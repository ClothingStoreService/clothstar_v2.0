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
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.repository.order.UpperOrderRepository;
import org.store.clothstar.order.repository.orderSeller.UpperOrderSellerRepository;
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
class OrderSellerServiceApplicationTest {

    @InjectMocks
    private OrderSellerService orderSellerService;

    @Mock
    private Order mockOrder;

    @Mock
    private UpperOrderSellerRepository upperOrderSellerRepository;

    @Mock
    private UpperOrderRepository upperOrderRepository;

    @Mock
    private OrderDetailService orderDetailService;

    @Test
    @DisplayName("getWaitingOrders: '승인대기' 주문 조회 - 메서드 호출 & 반환값 테스트")
    void getWaitingOrder_test() {
        //given
        Order order1 = mock(Order.class);
        given(order1.getCreatedAt()).willReturn(LocalDateTime.now());
        given(order1.getTotalShippingPrice()).willReturn(1000);

        Order order2 = mock(Order.class);
        given(order2.getCreatedAt()).willReturn(LocalDateTime.now());

        Order order3 = mock(Order.class);
        given(order3.getCreatedAt()).willReturn(LocalDateTime.now());

        List<Order> orders = List.of(order1, order2, order3);
        given(upperOrderSellerRepository.SelectWaitingOrders()).willReturn(orders);

        //when
        List<OrderResponse> response = orderSellerService.getWaitingOrder();

        //then
        then(upperOrderSellerRepository).should(times(1)).SelectWaitingOrders();
        assertThat(response).isNotNull().hasSize(3);
        assertThat(response.get(0).getTotalShippingPrice()).isEqualTo(1000);
    }

    @Test
    @DisplayName("approveOrder: 판매자 주문 승인 - 메서드 호출 & 반환값 테스트")
    void approveOrder_verify_test() {
        //given
        Long orderId = 1L;
        given(mockOrder.getStatus()).willReturn(Status.WAITING);
        given(mockOrder.getCreatedAt()).willReturn(LocalDateTime.now());
        given(upperOrderRepository.getOrder(orderId)).willReturn(Optional.of(mockOrder));

        //when
        MessageDTO messageDTO = orderSellerService.approveOrder(orderId);

        //then
        then(upperOrderSellerRepository).should(times(1)).approveOrder(orderId);
        then(upperOrderRepository).should(times(2)).getOrder(orderId);
        assertThat(messageDTO.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(messageDTO.getMessage()).isEqualTo("주문이 정상적으로 승인 되었습니다.");
    }

    @Test
    @DisplayName("cancelOrder: 판매자 주문 취소 - 메서드 호출 & 반환값 테스트")
    void cancelOrder_verify_test() {
        // given
        Long orderId = mockOrder.getOrderId();
        given(mockOrder.getOrderId()).willReturn(1L);
        given(mockOrder.getStatus()).willReturn(Status.WAITING);
        given(mockOrder.getCreatedAt()).willReturn(LocalDateTime.now());
        given(upperOrderRepository.getOrder(orderId)).willReturn(Optional.of(mockOrder));

        //when
        MessageDTO messageDTO = orderSellerService.cancelOrder(orderId);

        //then
        then(upperOrderSellerRepository).should(times(1)).cancelOrder(orderId);
        then(upperOrderRepository).should(times(2)).getOrder(orderId);
        assertThat(messageDTO.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(messageDTO.getMessage()).isEqualTo("주문이 정상적으로 취소 되었습니다.");
    }

    @Test
    @DisplayName("approveOrder: 주문상태가 '승인대기'가 아닐 때 예외처리 테스트")
    void cancelOrApproveOrder_NotWAITING_exception_test() {

        //given
        Long orderId = 1L;
        given(upperOrderRepository.getOrder(orderId)).willReturn(Optional.of(mockOrder));
        given(mockOrder.getStatus()).willReturn(Status.DELIVERED);

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            orderSellerService.approveOrder(orderId);
        });

        //then
        assertEquals("400 BAD_REQUEST \"주문이 존재하지 않거나 상태가 'WAITING'이 아니어서 처리할 수 없습니다.\"", thrown.getMessage());
    }
}