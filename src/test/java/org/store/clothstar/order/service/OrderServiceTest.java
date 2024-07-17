package org.store.clothstar.order.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.domain.vo.AddressInfo;
import org.store.clothstar.member.service.AddressService;
import org.store.clothstar.member.service.MemberService;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.domain.vo.OrderDetailDTO;
import org.store.clothstar.order.domain.vo.Price;
import org.store.clothstar.order.domain.vo.TotalPrice;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.OrderRequestWrapper;
import org.store.clothstar.order.repository.order.OrderDetailRepository;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.product.service.ProductService;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.service.ProductLineService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

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
    private OrderSaveFacade orderSaveFacade;

    @Mock
    private Member member;

    @Mock
    private Address address;

    @Mock
    private Order order;

    @Mock
    private OrderDetail orderDetail;

    @Mock
    private ProductLine productLine;

    @Mock
    private Product product;

    @Mock
    private AddressInfo addressInfo;

    @Mock
    private TotalPrice totalPrice;

    @Mock
    private Price price;

    @Mock
    private Seller seller;

    @Mock
    private OrderUserRepository orderUserRepository;

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

        given(orderUserRepository.findByOrderIdAndDeletedAtIsNull(orderId)).willReturn(Optional.of(order));
        given(order.getMemberId()).willReturn(memberId);
        given(order.getAddressId()).willReturn(addressId);
        given(order.getCreatedAt()).willReturn(LocalDateTime.now());
        given(order.getOrderDetails()).willReturn(List.of(orderDetail));

        given(memberService.getMemberByMemberId(memberId)).willReturn(member);
        given(addressService.getAddressById(addressId)).willReturn(address);
        given(address.getAddressInfo()).willReturn(addressInfo);
        given(order.getTotalPrice()).willReturn(totalPrice);
        given(orderDetail.getPrice()).willReturn(price);
        given(orderDetail.getDeletedAt()).willReturn(null);
        given(orderDetail.getProductId()).willReturn(productId);
        given(orderDetail.getProductLineId()).willReturn(productLineId);
        given(productService.findByIdIn(List.of(productId))).willReturn(List.of(product));
        given(productLineService.findByIdIn(List.of(productLineId))).willReturn(List.of(productLine));
        given(product.getId()).willReturn(productId);
        given(productLine.getId()).willReturn(productLineId);
        given(productLine.getSeller()).willReturn(seller);

        OrderResponse expectedOrderResponse = OrderResponse.from(order, member, address);
        expectedOrderResponse.setterOrderDetailList(List.of(OrderDetailDTO.from(orderDetail, product, productLine)));

        // when
        OrderResponse orderResponse = orderService.getOrder(orderId);

        // then
        assertThat(orderResponse).usingRecursiveComparison().isEqualTo(expectedOrderResponse);

        then(orderUserRepository).should(times(1)).findByOrderIdAndDeletedAtIsNull(orderId);
        then(memberService).should(times(1)).getMemberByMemberId(memberId);
        then(addressService).should(times(1)).getAddressById(addressId);
        then(productService).should(times(1)).findByIdIn(List.of(productId));
        then(productLineService).should(times(1)).findByIdIn(List.of(productLineId));
    }

    @Test
    @DisplayName("getAllOrderOffsetPaging: Offset 페이징 - 메서드 호출 테스트")
    void getAllOrderOffsetPaging_verify_test() {
        // given
        Pageable pageable = mock(Pageable.class);

        // 모의 데이터 설정
        Long memberId = 1L;
        Long addressId = 2L;
        Long productId = 3L;
        Long productLineId = 4L;

        List<Order> waitingOrders = List.of(order);
        Page<Order> orderEntities = new PageImpl<>(waitingOrders, pageable, waitingOrders.size());
        given(orderUserRepository.findAll(pageable)).willReturn(orderEntities);
        given(order.getMemberId()).willReturn(memberId);
        given(order.getAddressId()).willReturn(addressId);
        given(order.getCreatedAt()).willReturn(LocalDateTime.now());
        given(order.getOrderDetails()).willReturn(List.of(orderDetail));

        given(memberService.getMemberByMemberId(memberId)).willReturn(member);
        given(addressService.getAddressById(addressId)).willReturn(address);
        given(address.getAddressInfo()).willReturn(addressInfo);
        given(order.getTotalPrice()).willReturn(totalPrice);
        given(orderDetail.getPrice()).willReturn(price);
        given(orderDetail.getDeletedAt()).willReturn(null);
        given(orderDetail.getProductId()).willReturn(productId);
        given(orderDetail.getProductLineId()).willReturn(productLineId);
        given(productService.findByIdIn(List.of(productId))).willReturn(List.of(product));
        given(productLineService.findByIdIn(List.of(productLineId))).willReturn(List.of(productLine));
        given(product.getId()).willReturn(productId);
        given(productLine.getId()).willReturn(productLineId);
        given(productLine.getSeller()).willReturn(seller);

        OrderResponse expectedOrderResponse = OrderResponse.from(order, member, address);
        List<OrderDetail> orderDetails = List.of(orderDetail);
        List<OrderDetailDTO> orderDetailDTOList = orderDetails.stream()
                .map(orderDetail -> OrderDetailDTO.from(orderDetail, product, productLine))
                .collect(Collectors.toList());
        expectedOrderResponse.setterOrderDetailList(orderDetailDTOList);

        // when
        Page<OrderResponse> orderResponses = orderService.getAllOrderOffsetPaging(pageable);

        // then
        assertThat(orderResponses).hasSize(waitingOrders.size());
        assertThat(orderResponses.getContent().get(0)).usingRecursiveComparison().isEqualTo(expectedOrderResponse);

        verify(orderUserRepository, times(1)).findAll(pageable);
        verify(memberService, times(1)).getMemberByMemberId(memberId);
        verify(addressService, times(1)).getAddressById(addressId);
        verify(productService, times(1)).findByIdIn(List.of(productId));
        verify(productLineService, times(1)).findByIdIn(List.of(productLineId));
    }

    @Test
    @DisplayName("getAllOrderSlicePaging: Slice 페이징 - 메서드 호출 테스트")
    void getAllOrderSlicePaging_verify_test() {
        // given
        Pageable pageable = mock(Pageable.class);

        Long memberId = 1L;
        Long addressId = 2L;
        Long productId = 3L;
        Long productLineId = 4L;

        List<Order> waitingOrders = List.of(order);
        Page<Order> pageOrderEntities = new PageImpl<>(waitingOrders, pageable, waitingOrders.size());
        given(orderUserRepository.findAll(pageable)).willReturn(pageOrderEntities);
        given(order.getMemberId()).willReturn(memberId);
        given(order.getAddressId()).willReturn(addressId);
        given(order.getCreatedAt()).willReturn(LocalDateTime.now());
        given(order.getOrderDetails()).willReturn(List.of(orderDetail));

        given(memberService.getMemberByMemberId(memberId)).willReturn(member);
        given(addressService.getAddressById(addressId)).willReturn(address);
        given(address.getAddressInfo()).willReturn(addressInfo);
        given(order.getTotalPrice()).willReturn(totalPrice);
        given(orderDetail.getPrice()).willReturn(price);
        given(orderDetail.getDeletedAt()).willReturn(null);
        given(orderDetail.getProductId()).willReturn(productId);
        given(orderDetail.getProductLineId()).willReturn(productLineId);
        given(productService.findByIdIn(List.of(productId))).willReturn(List.of(product));
        given(productLineService.findByIdIn(List.of(productLineId))).willReturn(List.of(productLine));
        given(product.getId()).willReturn(productId);
        given(productLine.getId()).willReturn(productLineId);
        given(productLine.getSeller()).willReturn(seller);

        OrderResponse expectedOrderResponse = OrderResponse.from(order, member, address);
        List<OrderDetail> orderDetails = List.of(orderDetail);
        List<OrderDetailDTO> orderDetailDTOList = orderDetails.stream()
                .map(orderDetail -> OrderDetailDTO.from(orderDetail, product, productLine))
                .collect(Collectors.toList());
        expectedOrderResponse.setterOrderDetailList(orderDetailDTOList);

        // when
        Slice<OrderResponse> orderResponses = orderService.getAllOrderSlicePaging(pageable);

        // then
        assertThat(orderResponses).hasSize(waitingOrders.size());
        assertThat(orderResponses.getContent().get(0)).usingRecursiveComparison().isEqualTo(expectedOrderResponse);

        verify(orderUserRepository, times(1)).findAll(pageable);
        verify(memberService, times(1)).getMemberByMemberId(memberId);
        verify(addressService, times(1)).getAddressById(addressId);
        verify(productService, times(1)).findByIdIn(List.of(productId));
        verify(productLineService, times(1)).findByIdIn(List.of(productLineId));
    }

    @Test
    @DisplayName("saveOrder: 주문 생성 - 메서드 호출 & 반환값 테스트")
    void saveOrder_verify_test() {
        //given
        OrderRequestWrapper orderRequestWrapper = mock(OrderRequestWrapper.class);
        Long expectedOrderId = 1L;

        given(orderSaveFacade.saveOrder(orderRequestWrapper)).willReturn(expectedOrderId);

        // when
        Long orderId = orderService.saveOrder(orderRequestWrapper);

        // then
        then(orderSaveFacade).should(times(1)).saveOrder(orderRequestWrapper);
        assertThat(orderId).isEqualTo(expectedOrderId);
    }

    @Test
    @DisplayName("confirmOrder: 구매 확정 - 성공 메서드 호출 테스트")
    void confirmOrder_verify_test() {
        //given
        Long orderId = 1L;
        mock(OrderResponse.class);

        given(orderUserRepository.findById(1L)).willReturn(Optional.of(order));
        given(order.getStatus()).willReturn(Status.DELIVERED);

        //when
        orderService.confirmOrder(orderId);

        //then
        then(orderUserRepository).should(times(1)).findById(orderId);
        then(orderUserRepository).should().save(order);
    }

    @Test
    @DisplayName("confirmOrder: 구매 확정 - 실패 예외처리 테스트")
    void confirmOrder_fail_exception_test() {
        //given
        Long orderId = 1L;

        given(order.getStatus()).willReturn(Status.APPROVE);
        given(orderUserRepository.findById(orderId)).willReturn(Optional.of(order));

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                orderService.confirmOrder(orderId));

        //then
        assertEquals("400 BAD_REQUEST \"주문 상태가 '배송완료'가 아니기 때문에 주문확정이 불가능합니다.\"", thrown.getMessage());
    }

    @Test
    @DisplayName("cancelOrder: 구매 취소 - 성공 메서드 호출 테스트")
    void cancelOrder_verify_test() {
        //given
        Long orderId = 1L;
        mock(OrderResponse.class);

        given(orderUserRepository.findById(1L)).willReturn(Optional.of(order));
        given(order.getStatus()).willReturn(Status.APPROVE);

        //when
        orderService.cancelOrder(orderId);

        //then
        then(orderUserRepository).should(times(1)).findById(orderId);
        then(orderUserRepository).should(times(1)).save(order);
        then(orderDetailService).should(times(1)).restoreStockByOrder(orderId);
    }

    @Test
    @DisplayName("cancelOrder: 구매 취소 - 실패 예외처리 테스트")
    void cancelOrder_fail_exception_test() {
        //given
        Long orderId = 1L;

        given(order.getStatus()).willReturn(Status.DELIVERED);
        given(orderUserRepository.findById(orderId)).willReturn(Optional.of(order));

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                orderService.cancelOrder(orderId));

        //then
        assertEquals("400 BAD_REQUEST \"'승인대기' 또는 '주문승인' 상태가 아니기 때문에 주문을 취소할 수 없습니다.\"", thrown.getMessage());
    }

    @Test
    @DisplayName("updateDeleteAt: 주문 삭제 - 메서드 호출 테스트")
    void updateDeleteAt_verify_test() {
        //given
        Long orderId = 1L;
        OrderDetail mockOrderDetail1 = mock(OrderDetail.class);
        OrderDetail mockOrderDetail2 = mock(OrderDetail.class);
        OrderDetail mockOrderDetail3 = mock(OrderDetail.class);
        List<OrderDetail> orderDetailList = List.of(mockOrderDetail1, mockOrderDetail2, mockOrderDetail3);
        given(orderUserRepository.findById(1L)).willReturn(Optional.of(order));
        given(orderDetailRepository.findOrderDetailListByOrderId(orderId)).willReturn(orderDetailList);

        //when
        orderService.updateDeleteAt(orderId);

        //then
        verify(mockOrderDetail1, times(1)).updateDeletedAt();
        verify(mockOrderDetail2, times(1)).updateDeletedAt();
        verify(mockOrderDetail3, times(1)).updateDeletedAt();
        then(orderUserRepository).should(times(1)).findById(orderId);
        then(orderDetailRepository).should().findOrderDetailListByOrderId(orderId);
        then(order).should(times(1)).updateDeletedAt();
    }

    @Test
    @DisplayName("updateDeleteAt: 주문 삭제 - order null 예외처리 테스트")
    void updateDeleteAt_orderNull_exception_test() {
        //given
        Long orderId = 1L;
        given(orderUserRepository.findById(1L)).willReturn(Optional.empty());

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
        given(orderUserRepository.findById(1L)).willReturn(Optional.of(order));
        given(order.getDeletedAt()).willReturn(LocalDateTime.now());

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                orderService.updateDeleteAt(orderId));

        //then
        assertEquals("400 BAD_REQUEST \"이미 삭제된 주문입니다.\"", thrown.getMessage());
    }
}