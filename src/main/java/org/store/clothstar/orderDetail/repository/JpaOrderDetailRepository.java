package org.store.clothstar.orderDetail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;

@Repository
public interface JpaOrderDetailRepository extends JpaRepository<OrderDetailEntity,Long> {
}
