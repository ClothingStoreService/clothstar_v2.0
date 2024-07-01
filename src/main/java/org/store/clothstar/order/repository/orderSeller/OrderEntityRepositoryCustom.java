package org.store.clothstar.order.repository.orderSeller;

import org.store.clothstar.order.entity.OrderEntity;

import java.util.List;

public interface OrderEntityRepositoryCustom {
    List<OrderEntity> findWaitingOrders();
}
