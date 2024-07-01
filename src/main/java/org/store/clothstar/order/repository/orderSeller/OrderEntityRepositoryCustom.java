package org.store.clothstar.order.repository.orderSeller;

import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.entity.OrderEntity;

import java.util.List;

public interface OrderEntityRepositoryCustom {
    OrderResponse findOrderWithDetails(Long orderId);

    List<OrderEntity> findWaitingOrders();
}
