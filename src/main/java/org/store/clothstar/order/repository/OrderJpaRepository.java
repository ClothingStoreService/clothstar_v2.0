package org.store.clothstar.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.clothstar.order.entity.OrderEntity;

import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findById(Long id);
}
