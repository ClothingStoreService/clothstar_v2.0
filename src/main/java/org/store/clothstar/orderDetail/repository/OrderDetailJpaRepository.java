package org.store.clothstar.orderDetail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;

public interface OrderDetailJpaRepository extends JpaRepository<OrderDetailEntity, Long> {
}
