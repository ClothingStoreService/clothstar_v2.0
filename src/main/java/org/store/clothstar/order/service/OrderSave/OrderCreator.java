package org.store.clothstar.order.service.OrderSave;

import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.dto.request.CreateOrderRequest;

public interface OrderCreator {
    Order createOrder(CreateOrderRequest request,Member member,Address address);
}
