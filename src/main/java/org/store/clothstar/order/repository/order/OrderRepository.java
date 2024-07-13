package org.store.clothstar.order.repository.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.store.clothstar.order.entity.OrderEntity;

import java.util.Optional;

public interface OrderRepository {

    Optional<OrderEntity> findById(Long orderId);

    OrderEntity save(OrderEntity orderEntity);

    void deliveredToConfirmOrder(Long orderId);

    Page<OrderEntity> findAll(Pageable pageable);

    Slice<OrderEntity> findAllBy(Pageable pageable);
}
