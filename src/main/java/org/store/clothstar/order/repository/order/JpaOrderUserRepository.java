package org.store.clothstar.order.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.store.clothstar.order.domain.Order;

import java.util.Optional;

public interface JpaOrderUserRepository extends JpaRepository<Order, Long>, OrderUserRepository {

    Optional<Order> findByOrderIdAndDeletedAtIsNull(Long orderId);

    @Transactional
    @Modifying
    @Query("UPDATE orders o SET o.status ='CONFIRM' WHERE o.orderId = :orderId")
    void confirmOrder(@Param("orderId") Long orderId);

    @Transactional
    @Modifying
    @Query("UPDATE orders o SET o.status ='CANCEL' WHERE o.orderId = :orderId")
    void cancelOrder(@Param("orderId") Long orderId);
}
