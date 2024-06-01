package org.store.clothstar.orderDetail.repository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.order.JpaOrderRepository;
import org.store.clothstar.orderDetail.domain.OrderDetail;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;

@Repository
public class JpaOrderDetailRepositoryAdapter implements UpperOrderDetailRepository {
    private final JpaOrderDetailRepository jpaOrderDetailRepository;
    JpaOrderRepository jpaOrderRepository;

    public JpaOrderDetailRepositoryAdapter(JpaOrderDetailRepository jpaOrderDetailRepository,
                                           JpaOrderRepository jpaOrderRepository){
        this.jpaOrderDetailRepository = jpaOrderDetailRepository;
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public void saveOrderDetail(OrderDetail orderDetail) {
        OrderDetailEntity orderDetailEntity = convertToOrderEntity(orderDetail);
        jpaOrderDetailRepository.save(orderDetailEntity);
    }

    private OrderDetailEntity convertToOrderEntity(OrderDetail orderDetail) {
        OrderEntity order = jpaOrderRepository.findById(orderDetail.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."));

        return OrderDetailEntity.builder()
                .orderDetailId(orderDetail.getOrderDetailId())
                .order(order)
                .productLineId(orderDetail.getProductLineId())
                .productId(orderDetail.getProductId())
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
