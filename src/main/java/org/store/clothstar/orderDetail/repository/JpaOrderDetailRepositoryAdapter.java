package org.store.clothstar.orderDetail.repository;

import org.springframework.stereotype.Repository;
import org.store.clothstar.orderDetail.domain.OrderDetail;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;

@Repository
public class JpaOrderDetailRepositoryAdapter implements UpperOrderDetailRepository {
    private final JpaOrderDetailRepository jpaOrderDetailRepository;

    public JpaOrderDetailRepositoryAdapter(JpaOrderDetailRepository jpaOrderDetailRepository){
        this.jpaOrderDetailRepository = jpaOrderDetailRepository;
    }

    @Override
    public void saveOrderDetail(OrderDetail orderDetail) {
        OrderDetailEntity orderDetailEntity = convertToOrderEntity(orderDetail);
        jpaOrderDetailRepository.save(orderDetailEntity);
    }

    private OrderDetailEntity convertToOrderEntity(OrderDetail orderDetail) {
        return OrderDetailEntity.builder()
                .orderDetailId(orderDetail.getOrderDetailId())
                .orderId(orderDetail.getOrderId())
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
