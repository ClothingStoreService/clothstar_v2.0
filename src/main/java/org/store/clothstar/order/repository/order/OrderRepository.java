package org.store.clothstar.order.repository.order;

import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.entity.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Optional<OrderEntity> findById(Long orderId);

    OrderResponse findOrderWithDetails(Long orderId);

    OrderEntity save(OrderEntity orderEntity);

    void deliveredToConfirmOrder(Long orderId);

}
