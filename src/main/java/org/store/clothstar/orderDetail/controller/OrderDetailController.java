package org.store.clothstar.orderDetail.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.store.clothstar.order.utils.URIBuilder;
import org.store.clothstar.orderDetail.dto.request.AddOrderDetailRequest;
import org.store.clothstar.orderDetail.service.OrderDetailService;

import java.net.URI;

@Tag(name = "OrderDetail", description = "주문 내 개별 상품에 대한 옵션, 수량 등을 나타내는, 주문상세(OrderDetail) 정보 관리에 대한 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orderdetails")
public class OrderDetailController {

    private final OrderDetailService orderdetailService;

    @Operation(summary = "주문상세 추가 저장", description = "개별 상품에 대한 주문상세(상품명, 가격, 개수...)를 특정 주문에 추가 저장한다.")
    @PostMapping
    public ResponseEntity<URI> addOrderDetail(@RequestBody @Validated AddOrderDetailRequest addOrderDetailRequest) {
        Long orderDetailId = orderdetailService.addOrderDetail(addOrderDetailRequest);
        URI location = URIBuilder.buildURI(orderDetailId);
        return ResponseEntity.created(location).build();
    }
}
