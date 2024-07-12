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
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.domain.vo.AddressInfo;
import org.store.clothstar.member.service.AddressService;
import org.store.clothstar.member.service.MemberService;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.dto.request.OrderRequestWrapper;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.order.OrderRepository;
import org.store.clothstar.order.type.Status;
import org.store.clothstar.orderDetail.dto.OrderDetailDTO;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;
import org.store.clothstar.orderDetail.repository.OrderDetailRepository;
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

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

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

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Test
    @DisplayName("getOrder: 주문 조회 - 메서드 호출 & 반환값 테스트")
    void getOrder_test() {
        // given
        Long orderId = 1L;
        Long memberId = 2L;
        Long addressId = 3L;
        Long productId = 4L;
        Long productLineId = 5L;

        given(orderRepository.findById(orderId)).willReturn(Optional.of(orderEntity));
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
        OrderResponse orderResponse = orderService.getOrder(orderId);

        // then
        assertThat(orderResponse).usingRecursiveComparison().isEqualTo(expectedOrderResponse);

        then(orderRepository).should(times(1)).findById(orderId);
        then(memberService).should(times(1)).getMemberByMemberId(memberId);
        then(addressService).should(times(1)).getAddressById(addressId);
        then(productService).should(times(1)).findByIdIn(List.of(productId));
        then(productLineService).should(times(1)).findByIdIn(List.of(productLineId));
    }

//    @Test
//    @DisplayName("getAllOrderOffsetPaging: Offset 페이징 - 메서드 호출 테스트")
//    void getAllOrderOffsetPaging_verify_test() {
//        //given
//        Pageable pageable = mock(Pageable.class);
//
//        //when
//        orderService.getAllOrderOffsetPaging(pageable);
//
//        //then
//        then(orderRepository).should(times(1)).findAllOffsetPaging(pageable);
//    }
//
//    @Test
//    @DisplayName("getAllOrderSlicePaging: Slice 페이징 - 메서드 호출 테스트")
//    void getAllOrderSlicePaging_verify_test() {
//        //given
//        Pageable pageable = mock(Pageable.class);
//
//        //when
//        orderService.getAllOrderSlicePaging(pageable);
//
//        //then
//        then(orderRepository).should(times(1)).findAllSlicePaging(pageable);
//    }

    @Test
    @DisplayName("saveOrder: 주문 생성 - 메서드 호출 테스트")
    void saveOrder_verify_test() {
        //given
        OrderEntity orderEntity = mock(OrderEntity.class);
        OrderRequestWrapper orderRequestWrapper = mock(OrderRequestWrapper.class);
        CreateOrderRequest createOrderRequest = mock(CreateOrderRequest.class);
        Member mockmember = mock(Member.class);
        Address mockAddress = mock(Address.class);

        given(orderRequestWrapper.getCreateOrderRequest()).willReturn(createOrderRequest);
        given(createOrderRequest.getMemberId()).willReturn(1L);
        given(createOrderRequest.getAddressId()).willReturn(2L);

        given(memberService.getMemberByMemberId(createOrderRequest.getMemberId())).willReturn(mockmember);
        given(addressService.getAddressById(createOrderRequest.getAddressId())).willReturn(mockAddress);
        given(createOrderRequest.toOrderEntity(mockmember, mockAddress)).willReturn(orderEntity);

        //when
        orderService.saveOrder(orderRequestWrapper.getCreateOrderRequest());

        //then
        then(memberService).should(times(1)).getMemberByMemberId(createOrderRequest.getMemberId());
        then(addressService).should(times(1)).getAddressById(createOrderRequest.getAddressId());
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
        Member mockmember = mock(Member.class);
        Address mockAddress = mock(Address.class);

        given(orderEntity.getOrderId()).willReturn(1L);

        given(orderRequestWrapper.getCreateOrderRequest()).willReturn(createOrderRequest);

        given(createOrderRequest.getMemberId()).willReturn(1L);
        given(createOrderRequest.getAddressId()).willReturn(2L);

        given(memberService.getMemberByMemberId(1L)).willReturn(mockmember);
        given(addressService.getAddressById(2L)).willReturn(mockAddress);
        given(createOrderRequest.toOrderEntity(mockmember, mockAddress)).willReturn(orderEntity);

        //when
        Long orderId = orderService.saveOrder(orderRequestWrapper.getCreateOrderRequest());

        //then
        assertThat(orderId).isEqualTo(1L);
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
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                orderService.deliveredToConfirmOrder(orderId));

        //then
        assertEquals("400 BAD_REQUEST \"주문 상태가 '배송완료'가 아니기 때문에 주문확정이 불가능합니다.\"", thrown.getMessage());
    }

    @Test
    @DisplayName("updateDeleteAt: 주문 삭제 - 메서드 호출 테스트")
    void updateDeleteAt_verify_test() {
        //given
        Long orderId = 1L;
        OrderEntity orderEntity = mock(OrderEntity.class);
        OrderDetailEntity mockOrderDetail1 = mock(OrderDetailEntity.class);
        OrderDetailEntity mockOrderDetail2 = mock(OrderDetailEntity.class);
        OrderDetailEntity mockOrderDetail3 = mock(OrderDetailEntity.class);
        List<OrderDetailEntity> orderDetailList = List.of(mockOrderDetail1, mockOrderDetail2, mockOrderDetail3);
        given(orderRepository.findById(1L)).willReturn(Optional.of(orderEntity));
        given(orderDetailRepository.findOrderDetailListByOrderId(orderId)).willReturn(orderDetailList);

        //when
        orderService.updateDeleteAt(orderId);

        //then
        verify(mockOrderDetail1, times(1)).updateDeletedAt();
        verify(mockOrderDetail2, times(1)).updateDeletedAt();
        verify(mockOrderDetail3, times(1)).updateDeletedAt();
        then(orderRepository).should(times(1)).findById(orderId);
        then(orderDetailRepository).should().findOrderDetailListByOrderId(orderId);
        then(orderEntity).should(times(1)).updateDeletedAt();
    }

    @Test
    @DisplayName("updateDeleteAt: 주문 삭제 - orderEntity null 예외처리 테스트")
    void updateDeleteAt_orderEntityNull_exception_test() {
        //given
        Long orderId = 1L;
        given(orderRepository.findById(1L)).willReturn(Optional.empty());

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                orderService.updateDeleteAt(orderId));

        //then
        assertEquals("404 NOT_FOUND \"주문 번호를 찾을 수 없습니다.\"", thrown.getMessage());
    }

    @Test
    @DisplayName("updateDeleteAt: 주문 삭제 - 이미 삭제된 경우 예외처리 테스트")
    void updateDeleteAt_alreadyDelete_exception_test() {
        //given
        Long orderId = 1L;
        OrderEntity orderEntity = mock(OrderEntity.class);
        given(orderRepository.findById(1L)).willReturn(Optional.of(orderEntity));
        given(orderEntity.getDeletedAt()).willReturn(LocalDateTime.now());

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                orderService.updateDeleteAt(orderId));

        //then
        assertEquals("400 BAD_REQUEST \"이미 삭제된 주문입니다.\"", thrown.getMessage());
    }
}