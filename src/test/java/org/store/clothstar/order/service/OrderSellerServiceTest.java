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
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.domain.vo.OrderDetailDTO;
import org.store.clothstar.order.domain.vo.Price;
import org.store.clothstar.order.domain.vo.TotalPrice;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.repository.order.OrderUserRepository;
import org.store.clothstar.order.repository.orderSeller.OrderSellerRepository;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.service.ProductService;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.service.ProductLineService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private OrderUserRepository orderUserRepository;

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
    private Order order;

    @Mock
    private OrderDetail orderDetail;

    @Mock
    private ProductLineEntity productLineEntity;

    @Mock
    private ProductEntity productEntity;

    @Mock
    private AddressInfo addressInfo;

    @Mock
    private TotalPrice totalPrice;

    @Mock
    private Price price;

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

        //TODO 추후 개선 예정
        List<Order> waitingOrders = List.of(order);
        given(orderSellerRepository.findWaitingOrders()).willReturn(waitingOrders);
        given(order.getMemberId()).willReturn(memberId);
        given(order.getAddressId()).willReturn(addressId);
        given(memberService.getMemberByMemberId(memberId)).willReturn(member);
        given(address.getAddressInfo()).willReturn(addressInfo);
        given(addressService.getAddressById(addressId)).willReturn(address);
        given(order.getCreatedAt()).willReturn(LocalDateTime.now());
        given(order.getOrderDetails()).willReturn(List.of(orderDetail));
        given(orderDetail.getDeletedAt()).willReturn(null);

        given(order.getTotalPrice()).willReturn(totalPrice);
        given(orderDetail.getPrice()).willReturn(price);
        given(orderDetail.getProductId()).willReturn(productId);
        given(orderDetail.getProductLineId()).willReturn(productLineId);
        given(productService.findByIdIn(List.of(productId))).willReturn(List.of(productEntity));
        given(productLineService.findByIdIn(List.of(productLineId))).willReturn(List.of(productLineEntity));
        given(productEntity.getId()).willReturn(productId);
        given(productLineEntity.getId()).willReturn(productLineId);
        given(productLineEntity.getSeller()).willReturn(seller);

        OrderResponse expectedOrderResponse = OrderResponse.from(order, member, address);
        List<OrderDetail> orderDetails = List.of(orderDetail);
        List<OrderDetailDTO> orderDetailDTOList = orderDetails.stream()
                .map(orderDetail -> OrderDetailDTO.from(orderDetail, productEntity, productLineEntity))
                .collect(Collectors.toList());
        expectedOrderResponse.setterOrderDetailList(orderDetailDTOList);

        // when
        List<OrderResponse> orderResponses = orderSellerService.getWaitingOrder();

        // then
        assertThat(orderResponses).hasSize(waitingOrders.size());
        assertThat(orderResponses.get(0)).usingRecursiveComparison().isEqualTo(expectedOrderResponse);

        verify(orderSellerRepository, times(1)).findWaitingOrders();
        verify(memberService, times(1)).getMemberByMemberId(memberId);
        verify(addressService, times(1)).getAddressById(addressId);
        verify(productService, times(1)).findByIdIn(List.of(productId));
        verify(productLineService, times(1)).findByIdIn(List.of(productLineId));
    }

    @Test
    @DisplayName("approveOrder: 판매자 주문 승인 - 메서드 호출 & 반환값 테스트")
    void approveOrder_verify_test() {
        //given
        Long orderId = 1L;
        given(order.getStatus()).willReturn(Status.WAITING);
        given(orderUserRepository.findById(orderId)).willReturn(Optional.of(order));

        //when
        MessageDTO messageDTO = orderSellerService.approveOrder(orderId);

        //then
        then(orderSellerRepository).should(times(1)).save(order);
        then(orderUserRepository).should(times(1)).findById(orderId);
        assertThat(messageDTO.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(messageDTO.getMessage()).isEqualTo("주문이 정상적으로 승인 되었습니다.");
    }

    @Test
    @DisplayName("cancelOrder: 판매자 주문 취소 - 메서드 호출 & 반환값 테스트")
    void cancelOrder_verify_test() {
        // given
        Long orderId = 1L;
        given(order.getStatus()).willReturn(Status.WAITING);
        given(orderUserRepository.findById(orderId)).willReturn(Optional.of(order));

        //when
        MessageDTO messageDTO = orderSellerService.cancelOrder(orderId);

        //then
        then(orderSellerRepository).should(times(1)).save(order);
        then(orderUserRepository).should(times(1)).findById(orderId);
        assertThat(messageDTO.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(messageDTO.getMessage()).isEqualTo("주문이 정상적으로 취소 되었습니다.");
    }

    @Test
    @DisplayName("approveOrder: 주문상태가 '승인대기'가 아닐 때 예외처리 테스트")
    void approveOrder_NotWAITING_exception_test() {

        //given
        Long orderId = 1L;
        given(orderUserRepository.findById(orderId)).willReturn(Optional.of(order));
        given(order.getStatus()).willReturn(Status.DELIVERED);

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
            orderSellerService.approveOrder(orderId)
        );

        //then
        assertEquals("400 BAD_REQUEST \"주문이 존재하지 않거나 상태가 'WAITING'이 아니어서 처리할 수 없습니다.\"", thrown.getMessage());
    }

    @Test
    @DisplayName("cancelOrder: 주문상태가 '승인대기'가 아닐 때 예외처리 테스트")
    void cancelOrder_NotWAITING_exception_test() {

        //given
        Long orderId = 1L;
        given(orderUserRepository.findById(orderId)).willReturn(Optional.of(order));
        given(order.getStatus()).willReturn(Status.DELIVERED);

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
            orderSellerService.cancelOrder(orderId)
        );

        //then
        assertEquals("400 BAD_REQUEST \"주문이 존재하지 않거나 상태가 'WAITING'이 아니어서 처리할 수 없습니다.\"", thrown.getMessage());
    }
}