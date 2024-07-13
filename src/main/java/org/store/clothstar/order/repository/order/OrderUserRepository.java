package org.store.clothstar.order.repository.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.store.clothstar.order.domain.Order;

import java.util.Optional;

public interface OrderUserRepository {

    Optional<Order> findById(Long orderId);

    Page<Order> findAll(Pageable pageable);

    Order save(Order order);

    void confirmOrder(Long orderId);

    void cancelOrder(Long orderId);
}
