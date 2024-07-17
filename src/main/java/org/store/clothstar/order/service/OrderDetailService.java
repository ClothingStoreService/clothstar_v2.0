package org.store.clothstar.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.dto.request.AddOrderDetailRequest;
import org.store.clothstar.order.repository.order.OrderDetailRepository;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.product.repository.ProductRepository;
import org.store.clothstar.product.service.ProductService;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.repository.ProductLineRepository;

import java.util.List;

@Slf4j
@Service
public class OrderDetailService {
    private final OrderUserRepository orderUserRepository;
    private final ProductService productService;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final ProductLineRepository productLineRepository;

    public OrderDetailService(
            OrderDetailRepository orderDetailRepository,
            OrderUserRepository orderUserRepository, ProductService productService
            , ProductRepository productRepository
            , ProductLineRepository productLineRepository
    ) {
        this.orderUserRepository = orderUserRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productService = productService;
        this.productRepository = productRepository;
        this.productLineRepository = productLineRepository;
    }

    // 주문 생성시 같이 호출되는 주문 상세 생성 메서드 - 하나의 트랜잭션으로 묶임
    @Transactional
    public void saveOrderDetailWithOrder(CreateOrderDetailRequest createOrderDetailRequest, long orderId) {

        Order order = orderUserRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."));

        ProductLineEntity productLineEntity = productLineJPARepository.findById(createOrderDetailRequest.getProductLineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 옵션 정보를 찾을 수 없습니다."));

        ProductEntity productEntity = productJPARepository.findById(createOrderDetailRequest.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."));

        // 주문상세 생성 유효성 검사: 주문 수량이 상품 재고보다 클 경우, 주문이 생성되지 않는다.
        if (createOrderDetailRequest.getQuantity() > productEntity.getStock()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 개수가 재고보다 더 많습니다.");
        }

        OrderDetail orderDetail = createOrderDetailRequest.toOrderDetail(order, productLineEntity, productEntity);
        orderDetailRepository.save(orderDetail);

        // 주문 정보 업데이트: 주문 상세 생성에 따른, 총 상품 금액과 총 결제 금액 업데이트
        int newTotalProductsPrice = order.getTotalPrice().getProducts() + orderDetail.getPrice().getOneKindTotalPrice();
        int newTotalPaymentPrice =
                order.getTotalPrice().getProducts() + order.getTotalPrice().getShipping() + orderDetail.getPrice().getOneKindTotalPrice();

        order.getTotalPrice().updatePrices(newTotalProductsPrice, newTotalPaymentPrice);

        // 주문 수량만큼 상품 재고 차감
        updateProductStock(productEntity, orderDetail.getQuantity());
    }

    // 주문 상세 추가 생성
    @Transactional
    public Long addOrderDetail(AddOrderDetailRequest addOrderDetailRequest) {

        Order order = orderUserRepository.findById(addOrderDetailRequest.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."));

        ProductLineEntity productLineEntity = productLineJPARepository.findById(addOrderDetailRequest.getProductLineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 옵션 정보를 찾을 수 없습니다."));

        ProductEntity productEntity = productJPARepository.findById(addOrderDetailRequest.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."));

        if (addOrderDetailRequest.getQuantity() > product.getStock()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 개수가 재고보다 더 많습니다.");
        }

        if (!order.getStatus().equals(Status.WAITING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문이 이미 처리된 상태에서는 추가 주문이 불가능합니다.");
        }

        OrderDetail orderDetail = addOrderDetailRequest.toOrderDetail(order, productLine, product);
        orderDetailRepository.save(orderDetail);

        int newTotalProductsPrice = order.getTotalPrice().getProducts() + orderDetail.getPrice().getOneKindTotalPrice();
        int newTotalPaymentPrice =
                order.getTotalPrice().getProducts() + order.getTotalPrice().getShipping() + orderDetail.getPrice().getOneKindTotalPrice();

        order.getTotalPrice().updatePrices(newTotalProductsPrice, newTotalPaymentPrice);

        updateProductStock(product, orderDetail.getQuantity());

        return orderDetail.getOrderDetailId();
    }

    @Transactional
    public void updateDeleteAt(Long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문상세 번호를 찾을 수 없습니다."));

        if (orderDetail.getDeletedAt() != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "이미 삭제된 주문입니다.");
        }

        restoreStockByOrderDetail(orderDetailId);
        orderDetail.updateDeletedAt();
    }

    @Transactional
    public void updateProductStock(Product product, int quantity) {
        long updatedStock = product.getStock() - quantity;
        product.updateStock(updatedStock);
    }

    @Transactional
    public void restoreStockByOrder(Long orderId) {
        List<OrderDetail> orderDetailList = orderDetailRepository.findOrderDetailListByOrderId(orderId);
        productService.restoreProductStockByOrder(orderDetailList);
    }

    @Transactional
    public void restoreStockByOrderDetail(Long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문상세 번호를 찾을 수 없습니다."));
        productService.restoreProductStockByOrderDetail(orderDetail);
    }
}
