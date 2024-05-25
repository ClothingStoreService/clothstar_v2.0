package org.store.clothstar.order.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.order.domain.type.ApprovalStatus;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.OrderSellerRequest;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.OrderJpaRepository;
import org.store.clothstar.order.repository.OrderSellerJpaRepository;

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
    private OrderJpaRepository orderJpaRepository;

    @Test
    @DisplayName("getWaitingOrders 반환값 & 메서드 호출 테스트")
    void getWaitingOrder_test() {
        //given
        OrderEntity order1 = mock(OrderEntity.class);
        given(order1.getCreatedAt()).willReturn(LocalDateTime.now());
        given(order1.getTotalShippingPrice()).willReturn(1000);
        given(order1.getStatus()).willReturn(Status.WAITING);

        OrderEntity order2 = mock(OrderEntity.class);
        given(order2.getCreatedAt()).willReturn(LocalDateTime.now());
        given(order2.getStatus()).willReturn(Status.WAITING);

        OrderEntity order3 = mock(OrderEntity.class);
        given(order3.getCreatedAt()).willReturn(LocalDateTime.now());
        given(order3.getStatus()).willReturn(Status.WAITING);

        List<OrderEntity> orders = List.of(order1, order2, order3);
        given(orderSellerJpaRepository.findByStatus(Status.WAITING)).willReturn(orders);

        //when
        List<OrderResponse> response = orderSellerService.getWaitingOrder();

        //then
        then(orderSellerJpaRepository).should(times(1)).findByStatus(Status.WAITING);
        assertThat(response).isNotNull().hasSize(3);
        assertThat(response.get(0).getTotalShippingPrice()).isEqualTo(1000);
    }

    @Test
    @DisplayName("cancelOrApproveOrder: 주문상태 승인 메서드 호출 테스트")
    void approveOrder_verify_test() {

        OrderEntity order = mock(OrderEntity.class);
        given(order.getCreatedAt()).willReturn(LocalDateTime.now());
        given(order.getStatus()).willReturn(Status.WAITING);
        OrderSellerRequest orderSellerRequest = mock(OrderSellerRequest.class);
        given(orderSellerRequest.getApprovalStatus()).willReturn(ApprovalStatus.APPROVE);
        Long orderId = order.getOrderId();

        given(orderJpaRepository.findById(orderId)).willReturn(Optional.of(order));

        //when
        orderSellerService.cancelOrApproveOrder(orderId, orderSellerRequest);

        //then
        then(orderJpaRepository).should(times(3)).findById(orderId);
        then(orderSellerJpaRepository).should(times(1)).save(order);
    }

    @Test
    @DisplayName("cancelOrApproveOrder: 주문상태 취소 메서드 호출 테스트")
    void cancelOrApproveOrder_verify_test() {

        OrderEntity order = mock(OrderEntity.class);
        given(order.getCreatedAt()).willReturn(LocalDateTime.now());
        given(order.getStatus()).willReturn(Status.WAITING);
        OrderSellerRequest orderSellerRequest = mock(OrderSellerRequest.class);
        given(orderSellerRequest.getApprovalStatus()).willReturn(ApprovalStatus.CANCEL);
        Long orderId = order.getOrderId();

        given(orderJpaRepository.findById(orderId)).willReturn(Optional.of(order));

        //when
        orderSellerService.cancelOrApproveOrder(orderId, orderSellerRequest);

        //then
        then(orderJpaRepository).should(times(3)).findById(orderId);
        then(orderSellerJpaRepository).should(times(1)).save(order);
    }

    @Test
    @DisplayName("cancelOrApproveOrder - 주문상태가 WAITING이 아닐 때 예외처리 테스트")
    void cancelOrApproveOrder_NotWAITING_exception_test() {

        //given
        Long orderId = 1L;
        OrderEntity order = mock(OrderEntity.class);
        OrderSellerRequest mockOrderSellerRequest = mock(OrderSellerRequest.class);

        given(orderJpaRepository.findById(orderId)).willReturn(Optional.of(order));
        given(order.getStatus()).willReturn(Status.DELIVERED);

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            orderSellerService.cancelOrApproveOrder(orderId, mockOrderSellerRequest);
        });

        //then
        assertEquals("400 BAD_REQUEST \"주문이 존재하지 않거나 상태가 'WAITING'이 아니어서 처리할 수 없습니다.\"", thrown.getMessage());
    }
}