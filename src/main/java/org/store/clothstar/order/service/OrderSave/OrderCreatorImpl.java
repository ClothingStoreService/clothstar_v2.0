package org.store.clothstar.order.service.OrderSave;

import org.springframework.stereotype.Service;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.dto.request.CreateOrderRequest;


@Service
public class OrderCreatorImpl implements OrderCreator {

    @Override
    public Order createOrder(CreateOrderRequest request,Member member,Address address) {
        return request.toOrder(member, address);
    }
}
