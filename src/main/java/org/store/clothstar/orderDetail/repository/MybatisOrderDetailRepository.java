package org.store.clothstar.orderDetail.repository;

import org.apache.ibatis.annotations.Mapper;
import org.store.clothstar.orderDetail.domain.OrderDetail;

import java.util.List;

@Mapper
public interface MybatisOrderDetailRepository extends UpperOrderDetailRepository {

    void saveOrderDetail(OrderDetail orderdetail);

    List<OrderDetail> findByOrderId(Long orderId);
}
