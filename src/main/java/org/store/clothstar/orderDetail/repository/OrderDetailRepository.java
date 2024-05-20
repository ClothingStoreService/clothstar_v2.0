package org.store.clothstar.orderDetail.repository;

import org.apache.ibatis.annotations.Mapper;
import org.store.clothstar.orderDetail.domain.OrderDetail;

@Mapper
public interface OrderDetailRepository {

    void saveOrderDetail(OrderDetail orderdetail);
}
