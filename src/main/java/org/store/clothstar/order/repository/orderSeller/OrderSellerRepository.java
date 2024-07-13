package org.store.clothstar.order.repository.orderSeller;

import org.store.clothstar.order.domain.Order;

import java.util.List;

public interface OrderSellerRepository {

    List<Order> findWaitingOrders();

    void approveOrder(Long orderId);

    void cancelOrder(Long orderId);
}
