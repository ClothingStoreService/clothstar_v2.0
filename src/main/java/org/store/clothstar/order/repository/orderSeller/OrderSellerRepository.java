package org.store.clothstar.order.repository.orderSeller;

import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.entity.OrderEntity;

import java.util.List;

public interface OrderSellerRepository {

    List<OrderEntity> findWaitingOrders();

    void approveOrder(Long orderId);

    void cancelOrder(Long orderId);
}
