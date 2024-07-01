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
import org.store.clothstar.member.entity.AddressEntity;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.repository.AddressRepository;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.dto.request.OrderRequestWrapper;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.order.OrderRepository;
import org.store.clothstar.order.type.Status;

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

    @Test
    @DisplayName("getOrder: 주문 조회 - 메서드 호출 & 반환값 테스트")
    void getOrder_test() {
        //given
        Long orderId = 1L;
        OrderResponse mockOrderResponse = mock(OrderResponse.class);
        given(mockOrderResponse.getOrderId()).willReturn(orderId);
        given(orderRepository.findOrderWithDetails(orderId)).willReturn(mockOrderResponse);

        //when
        OrderResponse orderResponse = orderService.getOrder(orderId);

        //then
        then(orderRepository).should(times(1)).findOrderWithDetails(orderId);
        assertThat(orderResponse.getOrderId()).isEqualTo(orderId);
    }

    @Test
    @DisplayName("saveOrder: 주문 생성 - 메서드 호출 테스트")
    void saveOrder_verify_test() {
        //given
        OrderEntity orderEntity = mock(OrderEntity.class);
        OrderRequestWrapper orderRequestWrapper = mock(OrderRequestWrapper.class);
        CreateOrderRequest createOrderRequest = mock(CreateOrderRequest.class);
        MemberEntity mockmember = mock(MemberEntity.class);
        AddressEntity mockAddress = mock(AddressEntity.class);

        given(orderRequestWrapper.getCreateOrderRequest()).willReturn(createOrderRequest);
        given(createOrderRequest.getMemberId()).willReturn(1L);
        given(createOrderRequest.getAddressId()).willReturn(2L);

        given(memberRepository.findById(createOrderRequest.getMemberId())).willReturn(Optional.of(mockmember));
        given(addressRepository.findById(createOrderRequest.getAddressId())).willReturn(Optional.of(mockAddress));
        given(createOrderRequest.toOrderEntity(mockmember, mockAddress)).willReturn(orderEntity);

        //when
        orderService.saveOrder(orderRequestWrapper.getCreateOrderRequest());

        //then
        then(memberRepository).should(times(1)).findById(createOrderRequest.getMemberId());
        then(addressRepository).should(times(1)).findById(createOrderRequest.getAddressId());
        then(orderRepository).should(times(1)).save(orderEntity);
        verify(orderEntity).getOrderId();
    }

    @Test
    @DisplayName("saveOrder: 주문 생성 - 반환값 테스트")
    void saveOrder_test() {
        //given
        OrderEntity orderEntity = mock(OrderEntity.class);
        OrderRequestWrapper orderRequestWrapper = mock(OrderRequestWrapper.class);
        CreateOrderRequest createOrderRequest = mock(CreateOrderRequest.class);
        MemberEntity mockmember = mock(MemberEntity.class);
        AddressEntity mockAddress = mock(AddressEntity.class);

        given(orderEntity.getOrderId()).willReturn(1L);

        given(orderRequestWrapper.getCreateOrderRequest()).willReturn(createOrderRequest);

        given(createOrderRequest.getMemberId()).willReturn(1L);
        given(createOrderRequest.getAddressId()).willReturn(2L);

        given(memberRepository.findById(1L)).willReturn(Optional.of(mockmember));
        given(addressRepository.findById(2L)).willReturn(Optional.of(mockAddress));
        given(createOrderRequest.toOrderEntity(mockmember, mockAddress)).willReturn(orderEntity);

        //when
        Long orderId = orderService.saveOrder(orderRequestWrapper.getCreateOrderRequest());

        //then
        assertThat(orderId).isEqualTo(1L);
    }

    @Test
    @DisplayName("saveOrder: 주문 생성 - 회원 예외처리 테스트")
    void saveOrder_member_exception_test() {
        //given
        mock(OrderEntity.class);
        OrderRequestWrapper orderRequestWrapper = mock(OrderRequestWrapper.class);
        CreateOrderRequest createOrderRequest = mock(CreateOrderRequest.class);
        mock(Member.class);

        given(orderRequestWrapper.getCreateOrderRequest()).willReturn(createOrderRequest);
        given(createOrderRequest.getMemberId()).willReturn(1L);
        given(memberRepository.findById(1L)).willReturn(Optional.empty());

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            orderService.saveOrder(orderRequestWrapper.getCreateOrderRequest());
        });

        //then
        assertEquals("400 BAD_REQUEST \"회원 정보를 찾을 수 없습니다.\"", thrown.getMessage());
    }

    @Test
    @DisplayName("saveOrder: 주문 생성 - 배송지 예외처리 테스트")
    void saveOrder_address_exception_test() {
        //given
        mock(OrderEntity.class);
        OrderRequestWrapper orderRequestWrapper = mock(OrderRequestWrapper.class);
        CreateOrderRequest createOrderRequest = mock(CreateOrderRequest.class);
        MemberEntity mockmember = mock(MemberEntity.class);
        mock(Address.class);

        given(orderRequestWrapper.getCreateOrderRequest()).willReturn(createOrderRequest);
        given(createOrderRequest.getMemberId()).willReturn(1L);
        given(createOrderRequest.getAddressId()).willReturn(2L);
        given(memberRepository.findById(1L)).willReturn(Optional.of(mockmember));
        given(addressRepository.findById(2L)).willReturn(Optional.empty());

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            orderService.saveOrder(orderRequestWrapper.getCreateOrderRequest());
        });

        //then
        assertEquals("400 BAD_REQUEST \"배송지 정보를 찾을 수 없습니다.\"", thrown.getMessage());
    }

    @Test
    @DisplayName("deliveredToConfirmOrder: 구매 확정 - 성공 메서드 호출 테스트")
    void deliveredToConfirmOrder_verify_test() {
        //given
        Long orderId = 1L;
        OrderEntity orderEntity = mock(OrderEntity.class);
        mock(OrderResponse.class);

        given(orderRepository.findById(1L)).willReturn(Optional.of(orderEntity));
        given(orderEntity.getStatus()).willReturn(Status.DELIVERED);

        //when
        orderService.deliveredToConfirmOrder(orderId);

        //then
        then(orderRepository).should(times(1)).findById(orderId);
        then(orderRepository).should().deliveredToConfirmOrder(orderId);
    }

    @Test
    @DisplayName("deliveredToConfirmOrder: 구매 확정 - 실패 예외처리 테스트")
    void deliveredToConfirmOrder_fail_exception_test() {
        //given
        Long orderId = 1L;
        OrderEntity mockOrder = mock(OrderEntity.class);

        given(mockOrder.getStatus()).willReturn(Status.APPROVE);
        given(orderRepository.findById(orderId)).willReturn(Optional.of(mockOrder));

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            orderService.deliveredToConfirmOrder(orderId);
        });

        //then
        assertEquals("400 BAD_REQUEST \"주문 상태가 '배송완료'가 아니기 때문에 주문확정이 불가능합니다.\"", thrown.getMessage());
    }
}