package org.store.clothstar.orderDetail.service;

import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.repository.OrderRepository;
import org.store.clothstar.orderDetail.domain.OrderDetail;
import org.store.clothstar.orderDetail.dto.request.AddOrderDetailRequest;
import org.store.clothstar.orderDetail.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.orderDetail.repository.UpperOrderDetailRepository;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.product.repository.ProductRepository;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.repository.ProductLineMybatisRepository;

import java.util.List;

@Slf4j
@Service
//@RequiredArgsConstructor
public class OrderDetailService{
    private final UpperOrderDetailRepository upperOrderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductLineMybatisRepository productLineMybatisRepository;

    public OrderDetailService(
//            @Qualifier("jpaOrderDetailRepositoryAdapter") UpperOrderDetailRepository upperOrderDetailRepository
            @Qualifier("mybatisOrderDetailRepository") UpperOrderDetailRepository upperOrderDetailRepository
            ,OrderRepository orderRepository
            ,ProductRepository productRepository
            ,ProductLineRepository productLineRepository
    ){
        this.upperOrderDetailRepository = upperOrderDetailRepository;
        this.orderRepository  = orderRepository;
        this.productRepository  = productRepository;
        this.productLineRepository  = productLineRepository;
    }


    // 주문 생성시 같이 호출되는 주문 상세 생성 메서드 - 하나의 트랜잭션으로 묶임
    @Transactional
    public void saveOrderDetailWithOrder(CreateOrderDetailRequest createOrderDetailRequest, long orderId) {

        Order order = orderRepository.getOrder(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 옵션 정보를 찾을 수 없습니다."));

        ProductLine productLine = productLineMybatisRepository.selectByProductLineId(createOrderDetailRequest.getProductLineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 옵션 정보를 찾을 수 없습니다."));

        Product product = productRepository.selectByProductId(createOrderDetailRequest.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."));

        // 주문상세 생성 유효성 검사: 주문 수량이 상품 재고보다 클 경우, 주문이 생성되지 않는다.
        if (createOrderDetailRequest.getQuantity() > product.getStock()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 개수가 재고보다 더 많습니다.");
        }

        OrderDetail orderDetail = createOrderDetailRequest.toOrderDetail(orderId, productLine, product);
        upperOrderDetailRepository.saveOrderDetail(orderDetail);

        // 주문 정보 업데이트: 주문 상세 생성에 따른, 총 상품 금액과 총 결제 금액 업데이트
        int newTotalProductsPrice = order.getTotalProductsPrice() + orderDetail.getOneKindTotalPrice();
        int newTotalPaymentPrice =
                order.getTotalProductsPrice() + order.getTotalShippingPrice() + orderDetail.getOneKindTotalPrice();

        order.updatePrices(newTotalProductsPrice, newTotalPaymentPrice);
        orderRepository.updateOrderPrices(order);

        // 주문 수량만큼 상품 재고 차감
        updateProductStock(product, orderDetail.getQuantity());
    }

    @PermitAll
    // 주문 상세 추가 생성
    @Transactional
    public Long addOrderDetail(AddOrderDetailRequest addOrderDetailRequest) {

        Order order = orderRepository.getOrder(addOrderDetailRequest.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."));

        ProductLine productLine = productLineMybatisRepository.selectByProductLineId(addOrderDetailRequest.getProductLineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 옵션 정보를 찾을 수 없습니다."));

        Product product = productRepository.selectByProductId(addOrderDetailRequest.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."));

        if (addOrderDetailRequest.getQuantity() > product.getStock()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 개수가 재고보다 더 많습니다.");
        }

        OrderDetail orderDetail = addOrderDetailRequest.toOrderDetail(order, productLine, product);
        upperOrderDetailRepository.saveOrderDetail(orderDetail);

        int newTotalProductsPrice = order.getTotalProductsPrice() + orderDetail.getOneKindTotalPrice();
        int newTotalPaymentPrice =
                order.getTotalProductsPrice() + order.getTotalShippingPrice() + orderDetail.getOneKindTotalPrice();
        order.updatePrices(newTotalProductsPrice, newTotalPaymentPrice);
        orderRepository.updateOrderPrices(order);

        updateProductStock(product, orderDetail.getQuantity());

        return orderDetail.getOrderDetailId();
    }

    void updateProductStock(Product product, int quantity) {
        long updatedStock = product.getStock() - quantity;
        product.updateStock(updatedStock);
        productRepository.updateProduct(product);
    }

    public void restoreStockByOrder(Long orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);

        orderDetails.stream()
                .map(orderDetail -> {
                    Product product = productRepository.selectByProductId(orderDetail.getProductId())
                            .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"상품 정보를 찾을 수 없습니다."));
                    product.restoreStock(orderDetail.getQuantity());
                    return product;
                })
                .forEach(productRepository::updateProduct);
    }
}
