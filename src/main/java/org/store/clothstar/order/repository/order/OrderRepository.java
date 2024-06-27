package org.store.clothstar.order.repository.order;

import org.store.clothstar.order.entity.OrderEntity;

import java.util.Optional;

public interface OrderRepository {

    Optional<OrderEntity> findById(Long orderId);

    OrderEntity save(OrderEntity orderEntity);

    void deliveredToConfirmOrder(Long orderId);

    //없앤 이유는 JPA가 변경 자동 감지해서 업데이트 시켜주니까
//    void updateOrderPrices(Order order);
}
