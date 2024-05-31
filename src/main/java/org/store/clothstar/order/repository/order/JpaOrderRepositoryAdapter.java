package org.store.clothstar.order.repository.order;

import org.springframework.stereotype.Repository;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.entity.OrderEntity;

import java.util.Optional;

@Repository
public class JpaOrderRepositoryAdapter implements UpperOrderRepository {
    JpaOrderRepository jpaOrderRepository;

    JpaOrderRepositoryAdapter(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository=jpaOrderRepository;
    }

    @Override
    public Optional<Order> getOrder(Long orderId) {
        return jpaOrderRepository.findById(orderId)
                .map(Order::new);
    }

    @Override
    public int saveOrder(Order order) {
        return 0;
    }

    @Override
    public void deliveredToConfirmOrder(Long orderId) {
        jpaOrderRepository.deliveredToConfirmOrder(orderId);
    }

    @Override
    public void updateOrderPrices(Order order) {

    }
}
