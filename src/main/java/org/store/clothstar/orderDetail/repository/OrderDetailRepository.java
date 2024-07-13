package org.store.clothstar.orderDetail.repository;

import org.store.clothstar.orderDetail.entity.OrderDetailEntity;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository {
    Optional<OrderDetailEntity> findById(Long orderDetailId);

    OrderDetailEntity save(OrderDetailEntity orderdetailEntity);

    List<OrderDetailEntity> findOrderDetailListByOrderId(Long orderId);
}
