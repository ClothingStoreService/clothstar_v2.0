package org.store.clothstar.order.repository.orderSeller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.store.clothstar.order.dto.reponse.OrderResponse;

import java.util.List;

public interface OrderEntityRepositoryCustom {
    //Order 관련 메서드
    OrderResponse findOrderWithDetails(Long orderId);

    Page<OrderResponse> findAllOffsetPaging(Pageable pageable);

    Slice<OrderResponse> findAllSlicePaging(Pageable pageable);

    //OrderSeller 관련 메서드
    List<OrderResponse> findWaitingOrders();
}
