package org.store.clothstar.order.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.domain.vo.Price;
import org.store.clothstar.order.domain.vo.TotalPrice;
import org.store.clothstar.order.dto.request.AddOrderDetailRequest;
import org.store.clothstar.order.repository.order.OrderDetailRepository;
<<<<<<< HEAD:src/test/java/org/store/clothstar/order/service/OrderDetailServiceTest.java
import org.store.clothstar.order.repository.order.OrderUserRepository;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.repository.ProductJPARepository;
=======
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.product.repository.ProductRepository;
>>>>>>> a7a8e09 (refactor: productLine, product 엔티티, 레포지토리 클래스 이름 변경, Paging 개선):src/test/java/org/store/clothstar/orderDetail/service/OrderDetailServiceTest.java
import org.store.clothstar.product.service.ProductService;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.repository.ProductLineRepository;

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
    private OrderUserRepository orderUserRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private ProductLineRepository productLineRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderDetail orderDetail;

    @Mock
    private Order order;

    @Mock
    private ProductLine productLine;

    @Mock
    private Product product;

    @Mock
    private TotalPrice totalPrice;

    @Mock
    private Price price;

<<<<<<< HEAD:src/test/java/org/store/clothstar/order/service/OrderDetailServiceTest.java
=======
    @DisplayName("saveOrderDetailWithOrder: 주문상세 생성 - 메서드 호출 테스트")
    @Test
    void saveOrderDetailWithOrder_verify_test() {
        //given
        long orderId = 1L;
        CreateOrderDetailRequest mockRequest = mock(CreateOrderDetailRequest.class);

        given(orderUserRepository.findById(orderId)).willReturn(Optional.of(order));
        given(productLineRepository.findById(mockRequest.getProductLineId())).willReturn(Optional.of(productLine));
        given(productRepository.findById(mockRequest.getProductId())).willReturn(Optional.of(product));
        given(order.getTotalPrice()).willReturn(totalPrice);
        given(orderDetail.getPrice()).willReturn(price);
        given(mockRequest.toOrderDetail(order, productLine, product)).willReturn(orderDetail);

        //when
        orderDetailService.saveOrderDetailWithOrder(mockRequest, orderId);

        //then
        then(orderUserRepository).should(times(1)).findById(orderId);
        then(productLineRepository).should(times(1)).findById(mockRequest.getProductLineId());
        then(productRepository).should(times(1)).findById(mockRequest.getProductId());
        then(orderDetailRepository).should(times(1)).save(orderDetail);
    }

    @DisplayName("saveOrderDetailWithOrder: 주문상세 생성 - 주문 수량이 상품 재고보다 클 때 예외처리 테스트")
    @Test
    void saveOrderDetailWithOrder_exception_test() {
        //given
        long orderId = 1L;
        CreateOrderDetailRequest mockRequest = mock(CreateOrderDetailRequest.class);
        ProductLine mockProductLine = mock(ProductLine.class);
        Product mockProduct = mock(Product.class);

        given(orderUserRepository.findById(orderId)).willReturn(Optional.of(order));
        given(productLineRepository.findById(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productRepository.findById(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.getQuantity()).willReturn(10);
        given(mockProduct.getStock()).willReturn(1L);

        //when
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                orderDetailService.saveOrderDetailWithOrder(mockRequest, orderId));

        //then
        assertEquals("400 BAD_REQUEST \"주문 개수가 재고보다 더 많습니다.\"", thrown.getMessage());
    }

>>>>>>> a7a8e09 (refactor: productLine, product 엔티티, 레포지토리 클래스 이름 변경, Paging 개선):src/test/java/org/store/clothstar/orderDetail/service/OrderDetailServiceTest.java
    @DisplayName("addOrderDetail: 주문상세 추가 - 주문 유효성 검사 예외처리 테스트")
    @Test
    void getOrderDetail_quantityZero_exception_test() {
        //given
        AddOrderDetailRequest mockRequest = mock(AddOrderDetailRequest.class);
        ProductLine mockProductLine = mock(ProductLine.class);
        Product mockProduct = mock(Product.class);

        given(orderUserRepository.findById(mockRequest.getOrderId())).willReturn(Optional.of(order));
        given(productLineRepository.findById(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productRepository.findById(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
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
        ProductLine mockProductLine = mock(ProductLine.class);
        Product mockProduct = mock(Product.class);

        given(orderDetail.getOrderDetailId()).willReturn(1L);
        given(order.getStatus()).willReturn(Status.WAITING);
        given(order.getTotalPrice()).willReturn(totalPrice);
        given(orderDetail.getPrice()).willReturn(price);
        given(orderUserRepository.findById(mockRequest.getOrderId())).willReturn(Optional.of(order));
        given(productLineRepository.findById(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productRepository.findById(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.toOrderDetail(order, mockProductLine, mockProduct)).willReturn(orderDetail);

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
        ProductLine mockProductLine = mock(ProductLine.class);
        Product mockProduct = mock(Product.class);

        given(order.getStatus()).willReturn(Status.WAITING);
        given(order.getTotalPrice()).willReturn(totalPrice);
        given(orderDetail.getPrice()).willReturn(price);
        given(orderUserRepository.findById(mockRequest.getOrderId())).willReturn(Optional.of(order));
        given(productLineRepository.findById(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productRepository.findById(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
        given(mockRequest.toOrderDetail(order, mockProductLine, mockProduct)).willReturn(orderDetail);

        //when
        orderDetailService.addOrderDetail(mockRequest);

        //then
        then(orderUserRepository).should(times(1)).findById(mockRequest.getOrderId());
        then(productLineRepository).should(times(1)).findById(mockRequest.getProductLineId());
        then(productRepository).should(times(1)).findById(mockRequest.getProductId());
        then(orderDetailRepository).should(times(1)).save(orderDetail);
    }

    @DisplayName("addOrderDetail: 주문상세 추가 - 주문 유효성 검사 예외처리 테스트")
    @Test
    void addOrderDetail_quantityZero_exception_test() {
        //given
        AddOrderDetailRequest mockRequest = mock(AddOrderDetailRequest.class);
        ProductLine mockProductLine = mock(ProductLine.class);
        Product mockProduct = mock(Product.class);

        given(orderUserRepository.findById(mockRequest.getOrderId())).willReturn(Optional.of(order));
        given(productLineRepository.findById(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productRepository.findById(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));
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
        ProductLine mockProductLine = mock(ProductLine.class);
        Product mockProduct = mock(Product.class);

        given(order.getStatus()).willReturn(Status.CANCEL);
        given(orderUserRepository.findById(mockRequest.getOrderId())).willReturn(Optional.of(order));
        given(productLineRepository.findById(mockRequest.getProductLineId())).willReturn(Optional.of(mockProductLine));
        given(productRepository.findById(mockRequest.getProductId())).willReturn(Optional.of(mockProduct));

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
        given(orderDetailRepository.findById(orderDetailId)).willReturn(Optional.of(orderDetail));

        //when
        orderDetailService.updateDeleteAt(orderDetailId);

        //then
        then(orderDetailRepository).should(times(2)).findById(orderDetailId);
        then(productService).should(times(1)).restoreProductStockByOrderDetail(orderDetail);
        then(orderDetail).should(times(1)).updateDeletedAt();
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
        given(orderDetailRepository.findById(orderDetailId)).willReturn(Optional.of(orderDetail));
        given(orderDetail.getDeletedAt()).willReturn(LocalDateTime.now());

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
        OrderDetail mockOrderDetail1 = mock(OrderDetail.class);
        OrderDetail mockOrderDetail2 = mock(OrderDetail.class);
        OrderDetail mockOrderDetail3 = mock(OrderDetail.class);
        List<OrderDetail> orderDetailList = List.of(mockOrderDetail1, mockOrderDetail2, mockOrderDetail3);
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
        OrderDetail mockOrderDetail = mock(OrderDetail.class);
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