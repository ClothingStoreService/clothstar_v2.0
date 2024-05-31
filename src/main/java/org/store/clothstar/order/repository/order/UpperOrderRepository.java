package org.store.clothstar.order.repository.order;

import org.store.clothstar.order.domain.Order;

import java.util.Optional;

public interface UpperOrderRepository {

    Optional<Order> getOrder(Long orderId);

    int saveOrder(Order order);

    void deliveredToConfirmOrder(Long orderId);

    void updateOrderPrices(Order order);
}
