package org.store.clothstar.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.store.clothstar.orderDetail.dto.request.CreateOrderDetailRequest;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestWrapper {
    private CreateOrderRequest createOrderRequest;
    private CreateOrderDetailRequest createOrderDetailRequest;
}
