package org.store.clothstar.order.repository.orderSeller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.clothstar.order.entity.OrderEntity;

public interface JpaOrderSellerRepository extends JpaRepository<OrderEntity, Long> {

}
