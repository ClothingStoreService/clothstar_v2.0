package org.store.clothstar.orderDetail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.store.clothstar.orderDetail.domain.OrderDetail;

import java.util.List;

@Repository
public interface JpaOrderDetailRepository extends JpaRepository<OrderDetail, Long>, OrderDetailRepository {
    @Modifying
    @Query("SELECT od FROM order_detail od WHERE od.order.orderId = :orderId")
    List<OrderDetail> findOrderDetailListByOrderId(@Param("orderId") Long orderId);
}
