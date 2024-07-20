package org.store.clothstar.order.service.OrderSave;

import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;

public interface OrderPriceUpdater {
    void updateOrderPrice(Order order, OrderDetail orderDetail);
}
