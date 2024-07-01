package org.store.clothstar.orderDetail.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.order.OrderRepository;
import org.store.clothstar.orderDetail.dto.request.AddOrderDetailRequest;
import org.store.clothstar.orderDetail.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;
import org.store.clothstar.orderDetail.repository.OrderDetailRepository;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.repository.ProductJPARepository;
import org.store.clothstar.product.service.ProductService;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;

import java.util.List;

@Slf4j
@Service
public class OrderDetailService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductJPARepository productJPARepository;
    private final ProductLineJPARepository productLineJPARepository;

    public OrderDetailService(
            @Qualifier("jpaOrderDetailRepository") OrderDetailRepository orderDetailRepository,
            @Qualifier("jpaOrderRepository") OrderRepository orderRepository, ProductService productService
            , ProductJPARepository productJPARepository
            , ProductLineJPARepository productLineJPARepository
    ) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productService = productService;
        this.productJPARepository = productJPARepository;
        this.productLineJPARepository = productLineJPARepository;
    }


    // 주문 생성시 같이 호출되는 주문 상세 생성 메서드 - 하나의 트랜잭션으로 묶임
    @Transactional
    public void saveOrderDetailWithOrder(CreateOrderDetailRequest createOrderDetailRequest, long orderId) {

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."));

        ProductLineEntity productLineEntity = productLineJPARepository.findById(createOrderDetailRequest.getProductLineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 옵션 정보를 찾을 수 없습니다."));

        ProductEntity productEntity = productJPARepository.findById(createOrderDetailRequest.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."));

        // 주문상세 생성 유효성 검사: 주문 수량이 상품 재고보다 클 경우, 주문이 생성되지 않는다.
        if (createOrderDetailRequest.getQuantity() > productEntity.getStock()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 개수가 재고보다 더 많습니다.");
        }

        OrderDetailEntity orderDetailEntity = createOrderDetailRequest.toOrderDetailEntity(orderEntity, productLineEntity, productEntity);
        orderDetailRepository.save(orderDetailEntity);

        // 주문 정보 업데이트: 주문 상세 생성에 따른, 총 상품 금액과 총 결제 금액 업데이트
        int newTotalProductsPrice = orderEntity.getTotalProductsPrice() + orderDetailEntity.getOneKindTotalPrice();
        int newTotalPaymentPrice =
                orderEntity.getTotalProductsPrice() + orderEntity.getTotalShippingPrice() + orderDetailEntity.getOneKindTotalPrice();

        orderEntity.updatePrices(newTotalProductsPrice, newTotalPaymentPrice);
//        orderDetailRepository.updateOrderPrices(orderEntity);

        // 주문 수량만큼 상품 재고 차감
        updateProductStock(productEntity,orderDetailEntity.getQuantity());
    }

    // 주문 상세 추가 생성
    @Transactional
    public Long addOrderDetail(AddOrderDetailRequest addOrderDetailRequest) {

        OrderEntity orderEntity = orderRepository.findById(addOrderDetailRequest.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."));

        ProductLineEntity productLineEntity = productLineJPARepository.findById(addOrderDetailRequest.getProductLineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 옵션 정보를 찾을 수 없습니다."));

        ProductEntity productEntity = productJPARepository.findById(addOrderDetailRequest.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."));

        if (addOrderDetailRequest.getQuantity() > productEntity.getStock()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 개수가 재고보다 더 많습니다.");
        }

        OrderDetailEntity orderDetailEntity = addOrderDetailRequest.toOrderDetailEntity(orderEntity, productLineEntity, productEntity);
        orderDetailRepository.save(orderDetailEntity);

        int newTotalProductsPrice = orderEntity.getTotalProductsPrice() + orderDetailEntity.getOneKindTotalPrice();
        int newTotalPaymentPrice =
                orderEntity.getTotalProductsPrice() + orderEntity.getTotalShippingPrice() + orderDetailEntity.getOneKindTotalPrice();

        orderEntity.updatePrices(newTotalProductsPrice, newTotalPaymentPrice);
//        orderRepository.updateOrderPrices(orderEntity);

        updateProductStock(productEntity,orderDetailEntity.getQuantity());

        return orderDetailEntity.getOrderDetailId();
    }

    @Transactional
    void updateProductStock(ProductEntity productEntity, int quantity) {
        long updatedStock = productEntity.getStock() - quantity;
        productEntity.updateStock(updatedStock);
//        productJPARepository.updateProduct(productEntity);
    }

    @Transactional
    public void restoreStockByOrder(Long orderId) {
        List<OrderDetailEntity> orderDetailList = orderDetailRepository.findOrderDetailListByOrderId(orderId);

        orderDetailList.stream()
                .map(orderDetailEntity -> {
                    ProductEntity productEntity = productJPARepository.findById(orderDetailEntity.getProduct().getProductId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."));
                    productEntity.restoreStock(orderDetailEntity.getQuantity());
                    return productEntity;
                })
                .forEach(productJPARepository::save);
    }
}
