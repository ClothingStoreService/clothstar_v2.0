package org.store.clothstar.orderDetail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final OrderDetailJpaRepository orderDetailJpaRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final ProductRepository productRepository;
    private final ProductLineRepository productLineRepository;


    // 주문 생성시 같이 호출되는 주문 상세 생성 메서드 - 하나의 트랜잭션으로 묶임
    @Transactional
    public void saveOrderDetailWithOrder(CreateOrderDetailRequest createOrderDetailRequest, long orderId) {

        OrderEntity order = orderJpaRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."));

        ProductLine productLine = productLineRepository.selectByProductLineId(createOrderDetailRequest.getProductLineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 옵션 정보를 찾을 수 없습니다."));

        Product product = productRepository.selectByProductId(createOrderDetailRequest.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."));

        // 주문상세 생성 유효성 검사: 주문 수량이 상품 재고보다 클 경우, 주문이 생성되지 않는다.
        if (createOrderDetailRequest.getQuantity() > product.getStock()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 개수가 재고보다 더 많습니다.");
        }

        OrderDetailEntity orderDetail = createOrderDetailRequest.toOrderDetail(orderId, productLine, product);
        orderDetailJpaRepository.save(orderDetail);

        // 주문 정보 업데이트: 주문 상세 생성에 따른, 총 상품 금액과 총 결제 금액 업데이트
        int newTotalProductsPrice = order.getTotalProductsPrice() + orderDetail.getOneKindTotalPrice();
        int newTotalPaymentPrice =
                order.getTotalProductsPrice() + order.getTotalShippingPrice() + orderDetail.getOneKindTotalPrice();

        order.updatePrices(newTotalProductsPrice, newTotalPaymentPrice);
        orderJpaRepository.save(order);

        // 주문 수량만큼 상품 재고 차감
        updateProductStock(product, orderDetail.getQuantity());
    }

    // 주문 상세 추가 생성
    @Transactional
    public Long addOrderDetail(AddOrderDetailRequest addOrderDetailRequest) {

        OrderEntity order = orderJpaRepository.findById(addOrderDetailRequest.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."));

        ProductLine productLine = productLineRepository.selectByProductLineId(addOrderDetailRequest.getProductLineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 옵션 정보를 찾을 수 없습니다."));

        Product product = productRepository.selectByProductId(addOrderDetailRequest.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."));

        if (addOrderDetailRequest.getQuantity() > product.getStock()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 개수가 재고보다 더 많습니다.");
        }

        OrderDetailEntity orderDetail = addOrderDetailRequest.toOrderDetail(order, productLine, product);
        orderDetailJpaRepository.save(orderDetail);

        int newTotalProductsPrice = order.getTotalProductsPrice() + orderDetail.getOneKindTotalPrice();
        int newTotalPaymentPrice =
                order.getTotalProductsPrice() + order.getTotalShippingPrice() + orderDetail.getOneKindTotalPrice();
        order.updatePrices(newTotalProductsPrice, newTotalPaymentPrice);
        orderJpaRepository.save(order);

        updateProductStock(product, orderDetail.getQuantity());

        return orderDetail.getOrderDetailId();
    }

    void updateProductStock(Product product, int quantity) {
        long updatedStock = product.getStock() - quantity;
        product.updateStock(updatedStock);
        productRepository.updateProduct(product);
    }
}
