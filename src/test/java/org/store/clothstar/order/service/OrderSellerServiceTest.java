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
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.domain.vo.AddressInfo;
import org.store.clothstar.member.service.AddressService;
import org.store.clothstar.member.service.MemberService;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.order.OrderRepository;
import org.store.clothstar.order.repository.orderSeller.OrderSellerRepository;
import org.store.clothstar.order.type.Status;
import org.store.clothstar.orderDetail.dto.OrderDetailDTO;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;
import org.store.clothstar.orderDetail.service.OrderDetailService;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.service.ProductService;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.service.ProductLineService;

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
    private MemberService memberService;

    @Mock
    private AddressService addressService;

    @Mock
    private ProductLineService productLineService;

    @Mock
    private ProductService productService;

    @Mock
    private Member member;

    @Mock
    private Address address;

    @Mock
    private OrderEntity orderEntity;

    @Mock
    private OrderDetailEntity orderDetailEntity;

    @Mock
    private ProductLineEntity productLineEntity;

    @Mock
    private ProductEntity productEntity;

    @Mock
    private AddressInfo addressInfo;

    @Mock
    private Seller seller;

    @Test
    @DisplayName("getWaitingOrders: '승인대기' 주문 조회 - 메서드 호출 & 반환값 테스트")
    void getWaitingOrder_test() {
        // given
        Long memberId = 1L;
        Long addressId = 2L;
        Long productId = 3L;
        Long productLineId = 4L;

        given(orderSellerRepository.findWaitingOrders()).willReturn(List.of(orderEntity));
        given(orderEntity.getMemberId()).willReturn(memberId);
        given(orderEntity.getAddressId()).willReturn(addressId);
        given(orderEntity.getCreatedAt()).willReturn(LocalDateTime.now());
        given(orderEntity.getOrderDetails()).willReturn(List.of(orderDetailEntity));

        given(memberService.getMemberByMemberId(memberId)).willReturn(member);
        given(addressService.getAddressById(addressId)).willReturn(address);
        given(address.getAddressInfo()).willReturn(addressInfo);
        given(orderDetailEntity.getProductId()).willReturn(productId);
        given(orderDetailEntity.getProductLineId()).willReturn(productLineId);
        given(productService.findByIdIn(List.of(productId))).willReturn(List.of(productEntity));
        given(productLineService.findByIdIn(List.of(productLineId))).willReturn(List.of(productLineEntity));
        given(productEntity.getId()).willReturn(productId);
        given(productLineEntity.getId()).willReturn(productLineId);
        given(productLineEntity.getSeller()).willReturn(seller);

        OrderResponse expectedOrderResponse = OrderResponse.from(orderEntity, member, address);
        expectedOrderResponse.setterOrderDetailList(List.of(OrderDetailDTO.from(orderDetailEntity, productEntity, productLineEntity)));

        // when
        List<OrderResponse> orderResponses = orderSellerService.getWaitingOrder();

        // then
        assertThat(orderResponses.get(0)).isEqualToComparingFieldByFieldRecursively(expectedOrderResponse);

        then(orderSellerRepository).should(times(1)).findWaitingOrders();
        then(memberService).should(times(1)).getMemberByMemberId(memberId);
        then(addressService).should(times(1)).getAddressById(addressId);
        then(productService).should(times(1)).findByIdIn(List.of(productId));
        then(productLineService).should(times(1)).findByIdIn(List.of(productLineId));
    }

    @Test
    @DisplayName("approveOrder: 판매자 주문 승인 - 메서드 호출 & 반환값 테스트")
    void approveOrder_verify_test() {
        //given
        Long orderId = 1L;
        given(mockOrderEntity.getStatus()).willReturn(Status.WAITING);
        given(orderRepository.findById(orderId)).willReturn(Optional.of(mockOrderEntity));

        //when
        MessageDTO messageDTO = orderSellerService.approveOrder(orderId);

        //then
        then(orderSellerRepository).should(times(1)).approveOrder(orderId);
        then(orderRepository).should(times(1)).findById(orderId);
        assertThat(messageDTO.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(messageDTO.getMessage()).isEqualTo("주문이 정상적으로 승인 되었습니다.");
    }

    @Test
    @DisplayName("cancelOrder: 판매자 주문 취소 - 메서드 호출 & 반환값 테스트")
    void cancelOrder_verify_test() {
        // given
        Long orderId = 1L;
        given(mockOrderEntity.getStatus()).willReturn(Status.WAITING);
        given(orderRepository.findById(orderId)).willReturn(Optional.of(mockOrderEntity));

        //when
        MessageDTO messageDTO = orderSellerService.cancelOrder(orderId);

        //then
        then(orderSellerRepository).should(times(1)).cancelOrder(orderId);
        then(orderRepository).should(times(1)).findById(orderId);
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