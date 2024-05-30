package org.store.clothstar.orderDetail.repository;

import org.springframework.stereotype.Repository;
import org.store.clothstar.orderDetail.domain.OrderDetail;

@Repository
public class MybatisOrderDetailRepositoryAdapter implements UpperOrderDetailRepository{
    private final OrderDetailRepository orderDetailRepository;

    public MybatisOrderDetailRepositoryAdapter(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public void saveOrderDetail(OrderDetail orderDetail) {
        orderDetailRepository.saveOrderDetail(orderDetail);
    }
}
