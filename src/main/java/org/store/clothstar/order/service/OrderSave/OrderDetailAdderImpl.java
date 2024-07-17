package org.store.clothstar.order.service.OrderSave;

import org.springframework.stereotype.Service;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;

@Service
public class OrderDetailAdderImpl implements OrderDetailAdder {
    @Override
    public void addOrderDetail(Order order, OrderDetail orderDetail) {
        order.addOrderDetail(orderDetail);
    }
}
