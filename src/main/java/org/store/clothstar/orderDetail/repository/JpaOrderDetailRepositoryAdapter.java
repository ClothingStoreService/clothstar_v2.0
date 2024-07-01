package org.store.clothstar.orderDetail.repository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.order.JpaOrderRepository;
import org.store.clothstar.orderDetail.domain.OrderDetail;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.repository.ProductJPARepository;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JpaOrderDetailRepositoryAdapter implements UpperOrderDetailRepository {
    private final JpaOrderDetailRepository jpaOrderDetailRepository;
    private final ProductLineJPARepository productLineJPARepository;
    private final JpaOrderRepository jpaOrderRepository;
    private final ProductJPARepository productJPARepository;

    public JpaOrderDetailRepositoryAdapter(JpaOrderDetailRepository jpaOrderDetailRepository,
                                           JpaOrderRepository jpaOrderRepository,
                                           ProductLineJPARepository productLineJPARepository,
                                           ProductJPARepository productJPARepository
    ) {
        this.jpaOrderDetailRepository = jpaOrderDetailRepository;
        this.jpaOrderRepository = jpaOrderRepository;
        this.productLineJPARepository = productLineJPARepository;
        this.productJPARepository = productJPARepository;
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return jpaOrderDetailRepository.findByOrderId(orderId)
                .stream()
                .map(OrderDetail::new)
                .collect(Collectors.toList());
    }

    @Override
    public void saveOrderDetail(OrderDetail orderDetail) {
        OrderDetailEntity orderDetailEntity = convertToOrderEntity(orderDetail);
        jpaOrderDetailRepository.save(orderDetailEntity);
    }

    private OrderDetailEntity convertToOrderEntity(OrderDetail orderDetail) {
        OrderEntity order = jpaOrderRepository.findById(orderDetail.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."));

        ProductLineEntity productLine = productLineJPARepository.findById(orderDetail.getProductLineId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."));

        ProductEntity product = productJPARepository.findById(orderDetail.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품 옵션 정보를 찾을 수 없습니다."));

        return OrderDetailEntity.builder()
                .orderDetailId(orderDetail.getOrderDetailId())
                .order(order)
                .productLine(productLine)
                .product(product)
                .quantity(orderDetail.getQuantity())
                .fixedPrice(orderDetail.getFixedPrice())
                .oneKindTotalPrice(orderDetail.getOneKindTotalPrice())
                .name(orderDetail.getName())
                .stock(orderDetail.getStock())
                .optionName(orderDetail.getOptionName())
                .brandName(orderDetail.getBrandName())
                .build();
    }
}
