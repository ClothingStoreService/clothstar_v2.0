package org.store.clothstar.order.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.type.ApprovalStatus;
import org.store.clothstar.order.domain.type.PaymentMethod;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.OrderSellerRequest;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.OrderRepository;
import org.store.clothstar.order.repository.OrderSellerJpaRepository;
import org.store.clothstar.order.repository.OrderSellerRepository;

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
    private OrderSellerJpaRepository orderSellerJpaRepository;

    @Mock
    private OrderRepository orderRepository;

//    @Test
//    @DisplayName("getWaitingOrders 테스트")
//    void getWaitingOrder_test() {
//        //given
//        OrderEntity order1 = mock(OrderEntity.class);
//        given(order1.getCreatedAt()).willReturn(LocalDateTime.now());
//        given(order1.getTotalShippingPrice()).willReturn(1000);
//
//        OrderEntity order2 = mock(OrderEntity.class);
//        given(order2.getCreatedAt()).willReturn(LocalDateTime.now());
//
//        OrderEntity order3 = mock(OrderEntity.class);
//        given(order3.getCreatedAt()).willReturn(LocalDateTime.now());
//
//        List<OrderEntity> orders = List.of(order1, order2, order3);
//        given(orderSellerJpaRepository.findAll()).willReturn(orders);
//
//        //when
//        List<OrderResponse> response = orderSellerService.getWaitingOrder();
//
//        //then
//        then(orderSellerJpaRepository).should(times(1)).findAll();
//        assertThat(response).isNotNull().hasSize(3);
//        assertThat(response.get(0).getTotalShippingPrice()).isEqualTo(1000);
//    }

    // 판매자 주문상태 수정(승인/취소) 테스트
//    @Test
//    @DisplayName("cancelOrApproveOrder: 주문상태 승인 메서드 호출 테스트")
//    void approveOrder_verify_test() {
//
//        // given
//        Order order = Order.builder()
//                .orderId(1L)
//                .memberId(1L)
//                .addressId(1L)
//                .createdAt(LocalDateTime.now())
//                .status(Status.WAITING)
//                .totalShippingPrice(3000)
//                .totalProductsPrice(0)
//                .paymentMethod(PaymentMethod.CARD)
//                .totalPaymentPrice(0)
//                .build();
//
//        OrderSellerRequest orderSellerRequest = OrderSellerRequest.builder()
//                .approvalStatus(ApprovalStatus.APPROVE)
//                .build();
//
//        Long orderId = order.getOrderId();
//
//        given(orderRepository.getOrder(orderId)).willReturn(Optional.of(order));
//
//        //when
//        orderSellerService.cancelOrApproveOrder(orderId, orderSellerRequest);
//
//        //then
//        then(orderSellerRepository).should().approveOrder(orderId);
//    }
//
//    @Test
//    @DisplayName("cancelOrApproveOrder: 주문상태 취소 메서드 호출 테스트")
//    void cancelOrApproveOrder_verify_test() {
//
//        // given
//        Order order = Order.builder()
//                .orderId(1L)
//                .memberId(1L)
//                .addressId(1L)
//                .createdAt(LocalDateTime.now())
//                .status(Status.WAITING)
//                .totalShippingPrice(3000)
//                .totalProductsPrice(0)
//                .paymentMethod(PaymentMethod.CARD)
//                .totalPaymentPrice(0)
//                .build();
//
//        OrderSellerRequest orderSellerRequest = OrderSellerRequest.builder()
//                .approvalStatus(ApprovalStatus.CANCEL)
//                .build();
//
//        Long orderId = order.getOrderId();
//
//        given(orderRepository.getOrder(orderId)).willReturn(Optional.of(order));
//
//        //when
//        orderSellerService.cancelOrApproveOrder(orderId, orderSellerRequest);
//
//        //then
//        then(orderSellerRepository).should().cancelOrder(orderId);
//    }
//
//    @Test
//    @DisplayName("cancelOrApproveOrder: 메서드 호출 테스트")
//    void cancelOrder_verify_test() {
//        //given
//        Long orderId = 1L;
//        Order mockOrder = mock(Order.class);
//        mock(OrderResponse.class);
//        OrderSellerRequest mockOrderSellerRequest = mock(OrderSellerRequest.class);
//
//        given(orderRepository.getOrder(orderId)).willReturn(Optional.of(mockOrder));
//        given(mockOrder.getStatus()).willReturn(Status.WAITING);
//        given(mockOrder.getCreatedAt()).willReturn(LocalDateTime.now());
//        given(mockOrderSellerRequest.getApprovalStatus()).willReturn(ApprovalStatus.APPROVE);
//
//        //when
//        orderSellerService.cancelOrApproveOrder(orderId, mockOrderSellerRequest);
//
//        //then
//        then(orderRepository).should(times(2)).getOrder(orderId);
//        then(orderSellerRepository).should().approveOrder(orderId);
//    }
//
//    @Test
//    @DisplayName("cancelOrApproveOrder - 주문상태가 WAITING이 아닐 때 예외처리 테스트")
//    void cancelOrApproveOrder_NotWAITING_exception_test() {
//
//        //given
//        Long orderId = 1L;
//        Order mockOrder = mock(Order.class);
//        OrderSellerRequest mockOrderSellerRequest = mock(OrderSellerRequest.class);
//
//        given(orderRepository.getOrder(orderId)).willReturn(Optional.of(mockOrder));
//        given(mockOrder.getStatus()).willReturn(Status.DELIVERED);
//
//        //when
//        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
//            orderSellerService.cancelOrApproveOrder(orderId, mockOrderSellerRequest);
//        });
//
//        //then
//        assertEquals("400 BAD_REQUEST \"주문이 존재하지 않거나 상태가 'WAITING'이 아니어서 처리할 수 없습니다.\"", thrown.getMessage());
//    }
}