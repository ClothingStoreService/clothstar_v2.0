package org.store.clothstar.order.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.store.clothstar.order.entity.OrderEntity;

public interface JpaOrderRepository extends JpaRepository<OrderEntity, Long>, OrderRepository {
    @Transactional
    @Modifying
    @Query("UPDATE orders o SET o.status ='CONFIRM' WHERE o.orderId = :orderId")
    void deliveredToConfirmOrder(@Param("orderId") Long orderId);
}
