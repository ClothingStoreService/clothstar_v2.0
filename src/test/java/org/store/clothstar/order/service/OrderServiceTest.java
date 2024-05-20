package org.store.clothstar.order.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.repository.AddressRepository;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.dto.request.OrderRequestWrapper;
import org.store.clothstar.order.repository.OrderRepository;
import org.store.clothstar.orderDetail.service.OrderDetailService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private OrderDetailService orderDetailService;

    @Test
    @DisplayName("getOrder 주문 조회 테스트")
    void getOrder_test() {
        //given
        Order order = mock(Order.class);
        given(order.getOrderId()).willReturn(1L);
        given(order.getCreatedAt()).willReturn(LocalDateTime.now());

        given(orderRepository.getOrder(order.getOrderId())).willReturn(Optional.of(order));

        //when
        OrderResponse orderResponse = orderService.getOrder(order.getOrderId());

        //then
        assertThat(orderResponse.getOrderId()).isEqualTo(order.getOrderId());
    }

    @Test
    @DisplayName("getOrder 메서드 호출 테스트")
    void getOrder_verify_test() {
        //given
        Long orderId = 1L;
        Order mockOrder = mock(Order.class);
        mock(OrderResponse.class);

        given(orderRepository.getOrder(orderId)).willReturn(Optional.of(mockOrder));
        given(mockOrder.getCreatedAt()).willReturn(LocalDateTime.now());
        given(mockOrder.getStatus()).willReturn(Status.DELIVERED);

        //when
        orderService.getOrder(orderId);

        //then
        then(orderRepository).should().getOrder(orderId);
    }

    @Test
    @DisplayName("saveOrder 메서드 반환값 테스트")
    void saveOrder_test() {
        //given
        Order order = mock(Order.class);
        OrderRequestWrapper orderRequestWrapper = mock(OrderRequestWrapper.class);
        CreateOrderRequest createOrderRequest = mock(CreateOrderRequest.class);
        Member mockmember = mock(Member.class);
        Address mockAddress = mock(Address.class);

        given(order.getOrderId()).willReturn(1L);

        given(orderRequestWrapper.getCreateOrderRequest()).willReturn(createOrderRequest);

        given(createOrderRequest.getMemberId()).willReturn(1L);
        given(createOrderRequest.getAddressId()).willReturn(2L);

        given(memberRepository.findById(1L)).willReturn(Optional.of(mockmember));
        given(addressRepository.findById(2L)).willReturn(Optional.of(mockAddress));
        given(createOrderRequest.toOrder(mockmember, mockAddress)).willReturn(order);

        //when
        Long orderId = orderService.saveOrder(orderRequestWrapper.getCreateOrderRequest());

        //then
        assertThat(orderId).isEqualTo(1L);
    }

    @Test
    @DisplayName("saveOrder 메서드 호출 테스트")
    void saveOrder_verify_test() {
        //given
        Order order = mock(Order.class);
        OrderRequestWrapper orderRequestWrapper = mock(OrderRequestWrapper.class);
        CreateOrderRequest createOrderRequest = mock(CreateOrderRequest.class);
        Member mockmember = mock(Member.class);
        Address mockAddress = mock(Address.class);

        given(orderRequestWrapper.getCreateOrderRequest()).willReturn(createOrderRequest);
        given(createOrderRequest.getMemberId()).willReturn(1L);
        given(createOrderRequest.getAddressId()).willReturn(2L);

        given(memberRepository.findById(createOrderRequest.getMemberId())).willReturn(Optional.of(mockmember));
        given(addressRepository.findById(createOrderRequest.getAddressId())).willReturn(Optional.of(mockAddress));
        given(createOrderRequest.toOrder(mockmember, mockAddress)).willReturn(order);

        //when
        orderService.saveOrder(orderRequestWrapper.getCreateOrderRequest());

        //then
        then(memberRepository).should(times(1)).findById(createOrderRequest.getMemberId());
        then(addressRepository).should(times(1)).findById(createOrderRequest.getAddressId());
        then(orderRepository).should(times(1)).saveOrder(order);
        verify(order).getOrderId();
    }

    @Test
    @DisplayName("deliveredToConfirmOrder 메서드 호출 테스트")
    void deliveredToConfirmOrder_verify() {
        //given
        Long orderId = 1L;
        Order order = mock(Order.class);
        mock(OrderResponse.class);

        given(orderRepository.getOrder(1L)).willReturn(Optional.of(order));
        given(order.getStatus()).willReturn(Status.DELIVERED);

        //when
        orderService.deliveredToConfirmOrder(orderId);

        //then
        then(orderRepository).should(times(1)).getOrder(orderId);
        then(orderRepository).should().deliveredToConfirmOrder(orderId);
    }

    @Test
    @DisplayName("deliveredToConfirmOrder 성공 테스트")
    void deliveredToConfirmOrder_success_test() {
        //given
        Long orderId = 1L;
        Order mockOrder = mock(Order.class);

        given(mockOrder.getStatus()).willReturn(Status.DELIVERED);
        given(orderRepository.getOrder(orderId)).willReturn(Optional.of(mockOrder));

        //when
        orderService.deliveredToConfirmOrder(orderId);

        //then
        then(orderRepository).should().deliveredToConfirmOrder(orderId);
    }

    @Test
    @DisplayName("deliveredToConfirmOrder 실패 테스트")
    void deliveredToConfirmOrder_fail_test() {
        //given
        Long orderId = 1L;
        Order mockOrder = mock(Order.class);

        given(mockOrder.getStatus()).willReturn(Status.APPROVE);
        given(orderRepository.getOrder(orderId)).willReturn(Optional.of(mockOrder));

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            orderService.deliveredToConfirmOrder(orderId);
        });

        //then
        assertEquals("400 BAD_REQUEST \"주문 상태가 '배송완료'가 아니기 때문에 주문확정이 불가능합니다.\"", thrown.getMessage());
    }
}