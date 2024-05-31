package org.store.clothstar.orderDetail.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.repository.order.OrderRepository;
import org.store.clothstar.orderDetail.domain.OrderDetail;
import org.store.clothstar.orderDetail.dto.request.AddOrderDetailRequest;
import org.store.clothstar.orderDetail.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.orderDetail.repository.MybatisOrderDetailRepository;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.product.repository.ProductRepository;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.repository.ProductLineMybatisRepository;

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
    private OrderRepository orderRepository;

    @Mock
    private ProductLineMybatisRepository productLineMybatisRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MybatisOrderDetailRepository orderDetailRepository;

    @DisplayName("saveOrderDetailWithOrder: 주문상세 생성 - 메서드 호출 테스트")
    @Test
    void saveOrderDetailWithOrder_verify_test() {
        //given
        long orderId = 1L;
        CreateOrderDetailRequest mockRequest = mock(CreateOrderDetailRequest.class);
        OrderDetail mockOrderDetail = mock(OrderDetail.class);
        ProductLine mockProductLine = mock(ProductLine.class);
        Product mockProduct = mock(Product.class);
        Order mockOrder = mock(Order.class);

        given(orderRepository.getOrder(orderId)).willReturn(Optional.of(mockOrder));
        given(productLineMybatisRepository.selectByProductLineId(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productRepository.selectByProductId(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.toOrderDetail(orderId, mockProductLine, mockProduct)).willReturn(mockOrderDetail);

        //when
        orderDetailService.saveOrderDetailWithOrder(mockRequest, orderId);

        //then
        then(orderRepository).should().getOrder(orderId);
        then(productLineMybatisRepository).should().selectByProductLineId(mockRequest.getProductLineId());
        then(productRepository).should().selectByProductId(mockRequest.getProductId());
    }

    @DisplayName("addOrderDetail: 주문상세 추가 - 반환값 테스트")
    @Test
    void addOrderDetail_test() {
        //given
        AddOrderDetailRequest mockRequest = mock(AddOrderDetailRequest.class);
        OrderDetail mockOrderDetail = mock(OrderDetail.class);
        ProductLine mockProductLine = mock(ProductLine.class);
        Product mockProduct = mock(Product.class);
        Order mockOrder = mock(Order.class);

        given(mockOrderDetail.getOrderDetailId()).willReturn(1L);
        given(orderRepository.getOrder(mockRequest.getOrderId())).willReturn(Optional.of(mockOrder));
        given(productLineMybatisRepository.selectByProductLineId(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productRepository.selectByProductId(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.toOrderDetail(mockOrder, mockProductLine, mockProduct)).willReturn(mockOrderDetail);

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
        OrderDetail mockOrderDetail = mock(OrderDetail.class);
        ProductLine mockProductLine = mock(ProductLine.class);
        Product mockProduct = mock(Product.class);
        Order mockOrder = mock(Order.class);

        given(orderRepository.getOrder(mockRequest.getOrderId())).willReturn(Optional.of(mockOrder));
        given(productLineMybatisRepository.selectByProductLineId(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productRepository.selectByProductId(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.toOrderDetail(mockOrder, mockProductLine, mockProduct)).willReturn(mockOrderDetail);

        //when
        orderDetailService.addOrderDetail(mockRequest);

        //then
        then(orderRepository).should().getOrder(mockRequest.getOrderId());
        then(productLineMybatisRepository).should().selectByProductLineId(mockRequest.getProductLineId());
        then(productRepository).should().selectByProductId(mockRequest.getProductId());
    }

    @DisplayName("addOrderDetail: 주문상세 추가 - 주문 유효성 검사 예외처리 테스트")
    @Test
    void getOrderDetail_quantityZero_exception_test() {
        //given
        AddOrderDetailRequest mockRequest = mock(AddOrderDetailRequest.class);
        Order mockOrder = mock(Order.class);
        ProductLine mockProductLine = mock(ProductLine.class);
        Product mockProduct = mock(Product.class);

        given(orderRepository.getOrder(mockRequest.getOrderId())).willReturn(Optional.of(mockOrder));
        given(productLineMybatisRepository.selectByProductLineId(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productRepository.selectByProductId(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.getQuantity()).willReturn(10);
        given(mockProduct.getStock()).willReturn(1L);

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            orderDetailService.addOrderDetail(mockRequest);
        });

        //then
        assertEquals("400 BAD_REQUEST \"주문 개수가 재고보다 더 많습니다.\"", thrown.getMessage());
    }

    @Test
    @DisplayName("addOrderDetail: 주문생성시, 상품재고를 차감하는 메서드(updateProduct())가 호출되는지 테스트")
    void product_stock_subtract() {
        //given
        AddOrderDetailRequest mockRequest = mock(AddOrderDetailRequest.class);
        Order mockOrder = mock(Order.class);
        ProductLine mockProductLine = mock(ProductLine.class);
        Product mockProduct = mock(Product.class);
        OrderDetail mockOrderDetail = mock(OrderDetail.class);

        given(orderRepository.getOrder(mockRequest.getOrderId())).willReturn(Optional.of(mockOrder));
        given(productLineMybatisRepository.selectByProductLineId(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productRepository.selectByProductId(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.getQuantity()).willReturn(1);
        given(mockProduct.getStock()).willReturn(10L);
        given(mockRequest.toOrderDetail(mockOrder, mockProductLine, mockProduct)).willReturn(mockOrderDetail);

        //when
        orderDetailService.addOrderDetail(mockRequest);

        //then
        verify(productRepository).updateProduct(mockProduct);
    }

    @Test
    @DisplayName("restoreStockByOrder: 주문 취소시, 상품 재고 반환 - 메서드 호출 테스트")
    void restoreStockByOrder_verify_test() {
        //given
        long orderId = 1L;
        OrderDetail mockOrderDetail1 = mock(OrderDetail.class);
        OrderDetail mockOrderDetail2 = mock(OrderDetail.class);
        OrderDetail mockOrderDetail3 = mock(OrderDetail.class);
        Product mockProduct = mock(Product.class);
        List<OrderDetail> orderDetails = List.of(mockOrderDetail1, mockOrderDetail2, mockOrderDetail3);
        given(orderDetailRepository.findByOrderId(orderId)).willReturn(orderDetails);
        given(productRepository.selectByProductId(mockOrderDetail1.getProductId())).willReturn(Optional.of(mockProduct));

        //when
        orderDetailService.restoreStockByOrder(orderId);

        //then
        then(orderDetailRepository).should(times(1)).findByOrderId(orderId);
        then(productRepository).should(times(3)).selectByProductId(mockProduct.getProductId());
        then(productRepository).should(times(3)).updateProduct(mockProduct);

    }

    @Test
    @DisplayName("restoreStockByOrder - Product NULL 예외처리 테스트")
    void restoreStockByOrder_product_null_exception_test() {
        //given
        long orderId = 1L;
        OrderDetail mockOrderDetail1 = mock(OrderDetail.class);
        OrderDetail mockOrderDetail2 = mock(OrderDetail.class);
        OrderDetail mockOrderDetail3 = mock(OrderDetail.class);
        List<OrderDetail> orderDetails = List.of(mockOrderDetail1, mockOrderDetail2, mockOrderDetail3);
        given(orderDetailRepository.findByOrderId(orderId)).willReturn(orderDetails);
        given(productRepository.selectByProductId(mockOrderDetail1.getProductId())).willReturn(Optional.empty());

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            orderDetailService.restoreStockByOrder(orderId);
        });

        //then
        assertEquals("404 NOT_FOUND \"상품 정보를 찾을 수 없습니다.\"", thrown.getMessage());
    }
}