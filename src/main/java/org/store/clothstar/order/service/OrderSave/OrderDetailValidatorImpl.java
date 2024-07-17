package org.store.clothstar.order.service.OrderSave;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.product.entity.ProductEntity;

@Service
public class OrderDetailValidatorImpl implements OrderDetailValidator{
    @Override
    public void validateOrderDetail(CreateOrderDetailRequest request, ProductEntity product) {
        if (request.getQuantity() > product.getStock()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 개수가 재고보다 더 많습니다.");
        }
    }
}
