package org.store.clothstar.order.repository.orderSeller;

import org.apache.ibatis.annotations.Mapper;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.orderDetail.repository.UpperOrderDetailRepository;

import java.util.List;

@Mapper
public interface MybatisOrderSellerRepository extends UpperOrderDetailRepository {

    List<Order> SelectWaitingOrders();

    void approveOrder(Long orderId);

    void cancelOrder(Long orderId);
}
