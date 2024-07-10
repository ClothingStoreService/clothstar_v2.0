package org.store.clothstar.order.repository.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.entity.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Optional<OrderEntity> findById(Long orderId);

    OrderResponse findOrderWithDetails(Long orderId);

    Page<OrderResponse> findAllOffsetPaging(Pageable pageable);

    Slice<OrderResponse> findAllSlicePaging(Pageable pageable);

    OrderEntity save(OrderEntity orderEntity);

    void deliveredToConfirmOrder(Long orderId);
}
