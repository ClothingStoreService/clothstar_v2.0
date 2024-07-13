package org.store.clothstar.order.repository.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.store.clothstar.order.domain.Order;

import java.util.Optional;

public interface OrderRepository {

    Optional<Order> findById(Long orderId);

    Order save(Order orderEntity);

    void deliveredToConfirmOrder(Long orderId);

    Page<Order> findAll(Pageable pageable);

    Slice<Order> findAllBy(Pageable pageable);
}
