package org.store.clothstar.order.repository.orderSeller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.store.clothstar.order.domain.Order;

import java.util.List;

public interface JpaOrderSellerRepository extends JpaRepository<Order, Long>, OrderSellerRepository {

    @Query("SELECT o FROM orders o WHERE o.status = 'WAITING'")
    List<Order> findWaitingOrders();

    @Transactional
    @Modifying
    @Query("UPDATE orders o SET o.status = 'APPROVE' WHERE o.orderId = :orderId")
    void approveOrder(@Param("orderId") Long orderId);

    @Transactional
    @Modifying
    @Query("UPDATE orders o SET o.status = 'CANCEL' WHERE o.orderId = :orderId")
    void cancelOrder(@Param("orderId") Long orderId);
}
