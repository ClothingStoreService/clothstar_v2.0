package org.store.clothstar.orderDetail.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.common.dto.SaveResponseDTO;
import org.store.clothstar.orderDetail.dto.request.AddOrderDetailRequest;
import org.store.clothstar.orderDetail.service.OrderDetailService;

@Tag(name = "OrderDetail", description = "주문 내 개별 상품에 대한 옵션, 수량 등을 나타내는, 주문상세(OrderDetail) 정보 관리에 대한 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orderdetails")
public class OrderDetailController {

    private final OrderDetailService orderdetailService;

    @Operation(summary = "주문상세 추가 저장", description = "개별 상품에 대한 주문상세(상품명, 가격, 개수...)를 특정 주문에 추가 저장한다.")
    @PostMapping
    public ResponseEntity<SaveResponseDTO> addOrderDetail(@RequestBody @Validated AddOrderDetailRequest addOrderDetailRequest) {
        Long orderDetailId = orderdetailService.addOrderDetail(addOrderDetailRequest);
        return ResponseEntity.ok(new SaveResponseDTO(
                orderDetailId, HttpStatus.OK.value(), "주문상세가 정상적으로 생성되었습니다."));
    }

    @Operation(summary = "주문상세 삭제", description = "주문상세 삭제시간을 현재시간으로 업데이트 한다.")
    @DeleteMapping("{orderDetailId}")
    public ResponseEntity<MessageDTO> deleteOrderDetail(@PathVariable Long orderDetailId) {
        orderdetailService.updateDeleteAt(orderDetailId);
        return ResponseEntity.ok(new MessageDTO(HttpStatus.OK.value(), "주문상세가 정상적으로 삭제되었습니다."));
    }
}
