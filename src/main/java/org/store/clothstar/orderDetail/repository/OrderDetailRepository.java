package org.store.clothstar.orderDetail.repository;

import org.store.clothstar.orderDetail.entity.OrderDetailEntity;

import java.util.List;

public interface OrderDetailRepository {

    OrderDetailEntity save(OrderDetailEntity orderdetailEntity);

    List<OrderDetailEntity> findOrderDetailListByOrderId(Long orderId);
}
