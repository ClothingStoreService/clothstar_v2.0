package org.store.clothstar.order.service.OrderSave;

import org.store.clothstar.order.domain.Order;

public interface OrderSaver {
    void saveOrder(Order order);
}
