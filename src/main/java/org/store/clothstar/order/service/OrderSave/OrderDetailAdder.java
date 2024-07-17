package org.store.clothstar.order.service.OrderSave;

import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;

public interface OrderDetailAdder {
    void addOrderDetail(Order order, OrderDetail orderDetail);
}
