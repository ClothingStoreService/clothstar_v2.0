package org.store.clothstar.order.repository.orderSeller;

import org.store.clothstar.order.dto.reponse.OrderResponse;

import java.util.List;

public interface OrderSellerRepository {

//    List<OrderResponse> findWaitingOrders();

    void approveOrder(Long orderId);

    void cancelOrder(Long orderId);
}
