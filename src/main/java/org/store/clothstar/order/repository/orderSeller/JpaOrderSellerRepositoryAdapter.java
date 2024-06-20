package org.store.clothstar.order.repository.orderSeller;

import org.springframework.stereotype.Repository;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.entity.OrderEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JpaOrderSellerRepositoryAdapter implements UpperOrderSellerRepository {
    JpaOrderSellerRepository jpaOrderSellerRepository;

    JpaOrderSellerRepositoryAdapter(JpaOrderSellerRepository jpaOrderSellerRepository) {
        this.jpaOrderSellerRepository = jpaOrderSellerRepository;
    }

    @Override
    public List<Order> SelectWaitingOrders() {
        List<OrderEntity> orderEntityList = jpaOrderSellerRepository.findWaitingOrders();
        return orderEntityList.stream()
                .map(Order::new)
                .collect(Collectors.toList());
    }

    @Override
    public void approveOrder(Long orderId) {
        jpaOrderSellerRepository.approveOrder(orderId);
    }

    @Override
    public void cancelOrder(Long orderId) {
        jpaOrderSellerRepository.cancelOrder(orderId);
    }
}
