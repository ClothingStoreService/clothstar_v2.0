package org.store.clothstar.orderDetail.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.OrderJpaRepository;
import org.store.clothstar.orderDetail.dto.request.AddOrderDetailRequest;
import org.store.clothstar.orderDetail.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;
import org.store.clothstar.orderDetail.repository.OrderDetailJpaRepository;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.product.repository.ProductRepository;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.repository.ProductLineRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDetailServiceTest {

    @InjectMocks
    private OrderDetailService orderDetailService;

    @Mock
    private OrderJpaRepository orderJpaRepository;
    @Mock
    private ProductLineRepository productLineRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderDetailJpaRepository orderDetailJpaRepository;

    @DisplayName("saveOrderDetailWithOrder 메서드 호출 테스트")
    @Test
    void saveOrderDetailWithOrder_verify_test() {
        //given
        long orderId = 1L;
        CreateOrderDetailRequest mockRequest = mock(CreateOrderDetailRequest.class);
        OrderDetailEntity mockOrderDetail = mock(OrderDetailEntity.class);
        ProductLine mockProductLine = mock(ProductLine.class);
        Product mockProduct = mock(Product.class);
        OrderEntity mockOrder = mock(OrderEntity.class);

        given(orderJpaRepository.findById(orderId)).willReturn(Optional.of(mockOrder));
        given(productLineRepository.selectByProductLineId(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productRepository.selectByProductId(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.toOrderDetail(orderId, mockProductLine, mockProduct)).willReturn(mockOrderDetail);

        //when
        orderDetailService.saveOrderDetailWithOrder(mockRequest, orderId);

        //then
        then(orderJpaRepository).should().findById(orderId);
        then(productLineRepository).should().selectByProductLineId(mockRequest.getProductLineId());
        then(productRepository).should().selectByProductId(mockRequest.getProductId());
    }

    @DisplayName("addOrderDetail 주문 상세 추가 생성 테스트")
    @Test
    void addOrderDetail_test() {
        //given
        OrderDetailEntity orderDetail = OrderDetailEntity.builder()
                .orderDetailId(1L)
                .orderId(1L)
                .productLineId(1L)
                .productId(1L)
                .quantity(1)
                .fixedPrice(3000)
                .oneKindTotalPrice(3000)
                .name("워셔블 케이블 반팔 니트 세트")
                .stock(30L)
                .optionName("아이보리")
//                .extraCharge(0)
                .brandName("수아레")
                .build();

        AddOrderDetailRequest mockRequest = mock(AddOrderDetailRequest.class);
        ProductLine mockProductLine = mock(ProductLine.class);
        Product mockProduct = mock(Product.class);
        OrderEntity mockOrder = mock(OrderEntity.class);

        given(orderJpaRepository.findById(mockRequest.getOrderId())).willReturn(Optional.of(mockOrder));
        given(productLineRepository.selectByProductLineId(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productRepository.selectByProductId(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.toOrderDetail(mockOrder, mockProductLine, mockProduct)).willReturn(orderDetail);

        //when
        Long orderDetailResponse = orderDetailService.addOrderDetail(mockRequest);

        //then
        assertThat(orderDetailResponse).isEqualTo(1L);

    }

    @DisplayName("addOrderDetail 메서드 호출 테스트")
    @Test
    void addOrderDetail_verify_test() {
        //given
        AddOrderDetailRequest mockRequest = mock(AddOrderDetailRequest.class);
        OrderDetailEntity mockOrderDetail = mock(OrderDetailEntity.class);
        ProductLine mockProductLine = mock(ProductLine.class);
        Product mockProduct = mock(Product.class);
        OrderEntity mockOrder = mock(OrderEntity.class);

        given(orderJpaRepository.findById(mockRequest.getOrderId())).willReturn(Optional.of(mockOrder));
        given(productLineRepository.selectByProductLineId(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productRepository.selectByProductId(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.toOrderDetail(mockOrder, mockProductLine, mockProduct)).willReturn(mockOrderDetail);

        //when
        orderDetailService.addOrderDetail(mockRequest);

        //then
        then(orderJpaRepository).should().findById(mockRequest.getOrderId());
        then(productLineRepository).should().selectByProductLineId(mockRequest.getProductLineId());
        then(productRepository).should().selectByProductId(mockRequest.getProductId());
    }

    @DisplayName("addOrderDetail - 주문 유효성 검사 예외처리 테스트")
    @Test
    void getOrderDetail_quantityZero_exception_test() {
        //given
        AddOrderDetailRequest mockRequest = mock(AddOrderDetailRequest.class);
        OrderEntity mockOrder = mock(OrderEntity.class);
        ProductLine mockProductLine = mock(ProductLine.class);
        Product mockProduct = mock(Product.class);

        given(orderJpaRepository.findById(mockRequest.getOrderId())).willReturn(Optional.of(mockOrder));
        given(productLineRepository.selectByProductLineId(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
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
    @DisplayName("주문생성시, 상품재고를 차감하는 메서드(updateProduct())가 호출되는지 테스트")
    void product_stock_subtract() {
        //given
        AddOrderDetailRequest mockRequest = mock(AddOrderDetailRequest.class);
        OrderEntity mockOrder = mock(OrderEntity.class);
        ProductLine mockProductLine = mock(ProductLine.class);
        Product mockProduct = mock(Product.class);
        OrderDetailEntity mockOrderDetail = mock(OrderDetailEntity.class);

        given(orderJpaRepository.findById(mockRequest.getOrderId())).willReturn(Optional.of(mockOrder));
        given(productLineRepository.selectByProductLineId(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productRepository.selectByProductId(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.getQuantity()).willReturn(1);
        given(mockProduct.getStock()).willReturn(10L);
        given(mockRequest.toOrderDetail(mockOrder, mockProductLine, mockProduct)).willReturn(mockOrderDetail);

        //when
        orderDetailService.addOrderDetail(mockRequest);

        //then
        verify(productRepository).updateProduct(mockProduct);
    }
}