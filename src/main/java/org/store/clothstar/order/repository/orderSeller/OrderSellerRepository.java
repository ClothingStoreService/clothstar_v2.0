package org.store.clothstar.order.repository.orderSeller;

public interface OrderSellerRepository {

//    List<OrderResponse> findWaitingOrders();

    void approveOrder(Long orderId);

    void cancelOrder(Long orderId);
}
