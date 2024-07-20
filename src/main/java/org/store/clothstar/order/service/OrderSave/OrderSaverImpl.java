package org.store.clothstar.order.service.OrderSave;

import org.springframework.stereotype.Service;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.repository.order.OrderUserRepository;

@Service
public class OrderSaverImpl implements OrderSaver{
    private final OrderUserRepository orderUserRepository;

    public OrderSaverImpl(OrderUserRepository orderUserRepository) {
        this.orderUserRepository = orderUserRepository;
    }
    @Override
    public void saveOrder(Order order) {
        orderUserRepository.save(order);
    }
}
