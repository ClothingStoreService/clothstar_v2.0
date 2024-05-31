package org.store.clothstar.order.repository.order;

import org.apache.ibatis.annotations.Mapper;
import org.store.clothstar.order.domain.Order;

import java.util.Optional;

@Mapper
public interface OrderRepository {

    Optional<Order> getOrder(Long orderId);

    int saveOrder(Order order);

    void deliveredToConfirmOrder(Long orderId);

    void updateOrderPrices(Order order);
}
