package org.store.clothstar.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.entity.OrderEntity;

import java.util.List;

public interface OrderSellerJpaRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAll();

    List<OrderEntity> findByStatus(Status status);
}
