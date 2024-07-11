package org.store.clothstar.orderDetail.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.order.OrderRepository;
import org.store.clothstar.order.type.Status;
import org.store.clothstar.orderDetail.dto.request.AddOrderDetailRequest;
import org.store.clothstar.orderDetail.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;
import org.store.clothstar.orderDetail.repository.OrderDetailRepository;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.repository.ProductJPARepository;
import org.store.clothstar.product.service.ProductService;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDetailServiceTest {

    @InjectMocks
    private OrderDetailService orderDetailService;

    @Mock
    private ProductService productService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private ProductLineJPARepository productLineJPARepository;

    @Mock
    private ProductJPARepository productJPARepository;

    @Mock
    private OrderDetailEntity orderDetailEntity;

    @DisplayName("saveOrderDetailWithOrder: 주문상세 생성 - 메서드 호출 테스트")
    @Test
    void saveOrderDetailWithOrder_verify_test() {
        //given
        long orderId = 1L;
        CreateOrderDetailRequest mockRequest = mock(CreateOrderDetailRequest.class);
        OrderDetailEntity mockOrderDetail = mock(OrderDetailEntity.class);
        ProductLineEntity mockProductLine = mock(ProductLineEntity.class);
        ProductEntity mockProduct = mock(ProductEntity.class);
        OrderEntity mockOrder = mock(OrderEntity.class);

        given(orderRepository.findById(orderId)).willReturn(Optional.of(mockOrder));
        given(productLineJPARepository.findById(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productJPARepository.findById(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.toOrderDetailEntity(mockOrder, mockProductLine, mockProduct)).willReturn(mockOrderDetail);

        //when
        orderDetailService.saveOrderDetailWithOrder(mockRequest, orderId);

        //then
        then(orderRepository).should(times(1)).findById(orderId);
        then(productLineJPARepository).should(times(1)).findById(mockRequest.getProductLineId());
        then(productJPARepository).should(times(1)).findById(mockRequest.getProductId());
        then(orderDetailRepository).should(times(1)).save(mockOrderDetail);
//        then(orderRepository).should(times(1)).updateOrderPrices(mockOrder);
    }

    @DisplayName("saveOrderDetailWithOrder: 주문상세 생성 - 주문 수량이 상품 재고보다 클 때 예외처리 테스트")
    @Test
    void saveOrderDetailWithOrder_exception_test() {
        //given
        long orderId = 1L;
        CreateOrderDetailRequest mockRequest = mock(CreateOrderDetailRequest.class);
        ProductLineEntity mockProductLine = mock(ProductLineEntity.class);
        ProductEntity mockProduct = mock(ProductEntity.class);
        OrderEntity mockOrder = mock(OrderEntity.class);

        given(orderRepository.findById(orderId)).willReturn(Optional.of(mockOrder));
        given(productLineJPARepository.findById(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productJPARepository.findById(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.getQuantity()).willReturn(10);
        given(mockProduct.getStock()).willReturn(1L);

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                orderDetailService.saveOrderDetailWithOrder(mockRequest, orderId));

        //then
        assertEquals("400 BAD_REQUEST \"주문 개수가 재고보다 더 많습니다.\"", thrown.getMessage());
    }

    @DisplayName("addOrderDetail: 주문상세 추가 - 주문 유효성 검사 예외처리 테스트")
    @Test
    void getOrderDetail_quantityZero_exception_test() {
        //given
        AddOrderDetailRequest mockRequest = mock(AddOrderDetailRequest.class);
        ProductLineEntity mockProductLine = mock(ProductLineEntity.class);
        ProductEntity mockProduct = mock(ProductEntity.class);
        OrderEntity mockOrder = mock(OrderEntity.class);

        given(orderRepository.findById(mockRequest.getOrderId())).willReturn(Optional.of(mockOrder));
        given(productLineJPARepository.findById(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productJPARepository.findById(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.getQuantity()).willReturn(10);
        given(mockProduct.getStock()).willReturn(1L);

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                orderDetailService.addOrderDetail(mockRequest));

        //then
        assertEquals("400 BAD_REQUEST \"주문 개수가 재고보다 더 많습니다.\"", thrown.getMessage());
    }


    @DisplayName("addOrderDetail: 주문상세 추가 - 반환값 테스트")
    @Test
    void addOrderDetail_test() {
        //given
        AddOrderDetailRequest mockRequest = mock(AddOrderDetailRequest.class);
        OrderDetailEntity mockOrderDetail = mock(OrderDetailEntity.class);
        ProductLineEntity mockProductLine = mock(ProductLineEntity.class);
        ProductEntity mockProduct = mock(ProductEntity.class);
        OrderEntity mockOrder = mock(OrderEntity.class);

        given(mockOrderDetail.getOrderDetailId()).willReturn(1L);
        given(mockOrder.getStatus()).willReturn(Status.WAITING);
        given(orderRepository.findById(mockRequest.getOrderId())).willReturn(Optional.of(mockOrder));
        given(productLineJPARepository.findById(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productJPARepository.findById(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.toOrderDetailEntity(mockOrder, mockProductLine, mockProduct)).willReturn(mockOrderDetail);

        //when
        Long orderDetailId = orderDetailService.addOrderDetail(mockRequest);

        //then
        assertEquals(orderDetailId, 1L);
    }

    @DisplayName("addOrderDetail: 주문상세 추가 - 메서드 호출 테스트")
    @Test
    void addOrderDetail_verify_test() {
        //given
        AddOrderDetailRequest mockRequest = mock(AddOrderDetailRequest.class);
        OrderDetailEntity mockOrderDetail = mock(OrderDetailEntity.class);
        ProductLineEntity mockProductLine = mock(ProductLineEntity.class);
        ProductEntity mockProduct = mock(ProductEntity.class);
        OrderEntity mockOrder = mock(OrderEntity.class);

        given(mockOrder.getStatus()).willReturn(Status.WAITING);
        given(orderRepository.findById(mockRequest.getOrderId())).willReturn(Optional.of(mockOrder));
        given(productLineJPARepository.findById(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productJPARepository.findById(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.toOrderDetailEntity(mockOrder, mockProductLine, mockProduct)).willReturn(mockOrderDetail);

        //when
        orderDetailService.addOrderDetail(mockRequest);

        //then
        then(orderRepository).should(times(1)).findById(mockRequest.getOrderId());
        then(productLineJPARepository).should(times(1)).findById(mockRequest.getProductLineId());
        then(productJPARepository).should(times(1)).findById(mockRequest.getProductId());
        then(orderDetailRepository).should(times(1)).save(mockOrderDetail);
    }

    @DisplayName("addOrderDetail: 주문상세 추가 - 주문 유효성 검사 예외처리 테스트")
    @Test
    void addOrderDetail_quantityZero_exception_test() {
        //given
        AddOrderDetailRequest mockRequest = mock(AddOrderDetailRequest.class);
        ProductLineEntity mockProductLine = mock(ProductLineEntity.class);
        ProductEntity mockProduct = mock(ProductEntity.class);
        OrderEntity mockOrder = mock(OrderEntity.class);

        given(orderRepository.findById(mockRequest.getOrderId())).willReturn(Optional.of(mockOrder));
        given(productLineJPARepository.findById(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productJPARepository.findById(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.getQuantity()).willReturn(10);
        given(mockProduct.getStock()).willReturn(1L);

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                orderDetailService.addOrderDetail(mockRequest));

        //then
        assertEquals("400 BAD_REQUEST \"주문 개수가 재고보다 더 많습니다.\"", thrown.getMessage());
    }

    @DisplayName("addOrderDetail: 주문상세 추가 - 주문 상태 검사 예외처리 테스트")
    @Test
    void addOrderDetail_noWAITING_exception_test() {
        //given
        AddOrderDetailRequest mockRequest = mock(AddOrderDetailRequest.class);
        ProductLineEntity mockProductLine = mock(ProductLineEntity.class);
        ProductEntity mockProduct = mock(ProductEntity.class);
        OrderEntity mockOrder = mock(OrderEntity.class);

        given(mockOrder.getStatus()).willReturn(Status.CANCEL);
        given(orderRepository.findById(mockRequest.getOrderId())).willReturn(Optional.of(mockOrder));
        given(productLineJPARepository.findById(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productJPARepository.findById(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                orderDetailService.addOrderDetail(mockRequest));

        //then
        assertEquals("400 BAD_REQUEST \"주문이 이미 처리된 상태에서는 추가 주문이 불가능합니다.\"", thrown.getMessage());
    }

    @Test
    @DisplayName("updateDeleteAt: 주문 상세 삭제 - 메서드 호출 테스트")
    void updateDeleteAt_verify_test() {
        //given
        long orderDetailId = 1L;
        given(orderDetailRepository.findById(orderDetailId)).willReturn(Optional.of(orderDetailEntity));

        //when
        orderDetailService.updateDeleteAt(orderDetailId);

        //then
        then(orderDetailRepository).should(times(2)).findById(orderDetailId);
        then(productService).should(times(1)).restoreProductStockByOrderDetail(orderDetailEntity);
        then(orderDetailEntity).should(times(1)).updateDeletedAt();
    }

    @Test
    @DisplayName("updateDeleteAt: 주문 상세 삭제 - OrderDetail null 예외처리 테스트")
    void updateDeleteAt_null_exception_test() {
        //given
        long orderDetailId = 1L;
        given(orderDetailRepository.findById(orderDetailId)).willReturn(Optional.empty());

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                orderDetailService.updateDeleteAt(orderDetailId));

        //then
        assertEquals("404 NOT_FOUND \"주문상세 번호를 찾을 수 없습니다.\"", thrown.getMessage());
    }

    @Test
    @DisplayName("updateDeleteAt: 주문 상세 삭제 - 이미 삭제된 경우 예외처리 테스트")
    void updateDeleteAt_alreadyDelete_exception_test() {
        //given
        long orderDetailId = 1L;
        given(orderDetailRepository.findById(orderDetailId)).willReturn(Optional.of(orderDetailEntity));
        given(orderDetailEntity.getDeletedAt()).willReturn(LocalDateTime.now());

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                orderDetailService.updateDeleteAt(orderDetailId));

        //then
        assertEquals("404 NOT_FOUND \"이미 삭제된 주문입니다.\"", thrown.getMessage());
    }

    @Test
    @DisplayName("restoreStockByOrder: 주문 취소시, 상품 재고 반환 - 메서드 호출 테스트")
    void restoreStockByOrder_verify_test() {
        //given
        long orderId = 1L;
        OrderDetailEntity mockOrderDetail1 = mock(OrderDetailEntity.class);
        OrderDetailEntity mockOrderDetail2 = mock(OrderDetailEntity.class);
        OrderDetailEntity mockOrderDetail3 = mock(OrderDetailEntity.class);
        List<OrderDetailEntity> orderDetailList = List.of(mockOrderDetail1, mockOrderDetail2, mockOrderDetail3);
        given(orderDetailRepository.findOrderDetailListByOrderId(orderId)).willReturn(orderDetailList);

        //when
        orderDetailService.restoreStockByOrder(orderId);

        //then
        then(productService).should(times(1)).restoreProductStockByOrder(orderDetailList);
    }

    @Test
    @DisplayName("restoreStockByOrderDetail: 주문 상세 삭제시, 상품 재고 반환 - 메서드 호출 테스트")
    void restoreStockByOrderDetail_verify_test() {
        //given
        long orderDetailId = 1L;
        OrderDetailEntity mockOrderDetail = mock(OrderDetailEntity.class);
        given(orderDetailRepository.findById(orderDetailId)).willReturn(Optional.of(mockOrderDetail));

        //when
        orderDetailService.restoreStockByOrderDetail(orderDetailId);

        //then
        then(productService).should(times(1)).restoreProductStockByOrderDetail(mockOrderDetail);
    }

    @Test
    @DisplayName("restoreStockByOrderDetail: 주문 상세 삭제시, 상품 재고 반환 - orderDetail null 예외처리 테스트")
    void restoreStockByOrderDetail_null_exception_test() {
        //given
        long orderDetailId = 1L;
        given(orderDetailRepository.findById(orderDetailId)).willReturn(Optional.empty());

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                orderDetailService.restoreStockByOrderDetail(orderDetailId));

        //then
        assertEquals("404 NOT_FOUND \"주문상세 번호를 찾을 수 없습니다.\"", thrown.getMessage());
    }
}