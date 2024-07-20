package org.store.clothstar.order.service.OrderSave;

import org.springframework.stereotype.Service;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;

@Service
public class OrderPriceUpdaterImpl implements OrderPriceUpdater {
    @Override
    public void updateOrderPrice(Order order, OrderDetail orderDetail) {
        int newTotalProductsPrice = order.getTotalPrice().getProducts() + orderDetail.getPrice().getOneKindTotalPrice();
        int newTotalPaymentPrice =
                order.getTotalPrice().getProducts() + order.getTotalPrice().getShipping() + orderDetail.getPrice().getOneKindTotalPrice();

        order.getTotalPrice().updatePrices(newTotalProductsPrice, newTotalPaymentPrice);
    }
}
