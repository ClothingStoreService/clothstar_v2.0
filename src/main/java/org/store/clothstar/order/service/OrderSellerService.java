package org.store.clothstar.order.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.service.AddressService;
import org.store.clothstar.member.service.MemberService;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.order.OrderRepository;
import org.store.clothstar.order.repository.orderSeller.OrderSellerRepository;
import org.store.clothstar.order.type.Status;
import org.store.clothstar.orderDetail.dto.OrderDetailDTO;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;
import org.store.clothstar.orderDetail.service.OrderDetailService;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.service.ProductService;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.service.ProductLineService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderSellerService {

    private final OrderSellerRepository orderSellerRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final MemberService memberService;
    private final AddressService addressService;
    private final ProductService productService;
    private final ProductLineService productLineService;

    public OrderSellerService(
            OrderSellerRepository orderSellerRepository
            , OrderRepository orderRepository
            , OrderDetailService orderDetailService
            , MemberService memberService, AddressService addressService
            , ProductService productService, ProductLineService productLineService
    ) {
        this.orderSellerRepository = orderSellerRepository;
        this.orderRepository = orderRepository;
        this.orderDetailService=orderDetailService;
        this.memberService = memberService;
        this.addressService = addressService;
        this.productService = productService;
        this.productLineService = productLineService;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getWaitingOrder() {
        List<OrderEntity> waitingOrders = orderSellerRepository.findWaitingOrders();

        return waitingOrders.stream()
                .map(orderEntity -> {
                    Member member = memberService.getMemberByMemberId(orderEntity.getMemberId());
                    Address address = addressService.getAddressById(orderEntity.getAddressId());
                    OrderResponse orderResponse = OrderResponse.from(orderEntity, member, address);

                    List<OrderDetailEntity> orderDetails = orderEntity.getOrderDetails();
                    List<Long> productIds = orderDetails.stream().map(OrderDetailEntity::getProductId).collect(Collectors.toList());
                    List<Long> productLineIds = orderDetails.stream().map(OrderDetailEntity::getProductLineId).collect(Collectors.toList());

                    List<ProductEntity> products = productService.findByIdIn(productIds);
                    List<ProductLineEntity> productLines = productLineService.findByIdIn(productLineIds);

                    Map<Long, ProductEntity> productMap = products.stream().collect(Collectors.toMap(ProductEntity::getId, product -> product));
                    Map<Long, ProductLineEntity> productLineMap = productLines.stream().collect(Collectors.toMap(ProductLineEntity::getId, productLine -> productLine));

                    List<OrderDetailDTO> orderDetailDTOList = orderDetails.stream().map(orderDetailEntity -> {
                        ProductEntity productEntity = productMap.get(orderDetailEntity.getProductId());
                        ProductLineEntity productLineEntity = productLineMap.get(orderDetailEntity.getProductLineId());
                        return OrderDetailDTO.from(orderDetailEntity, productEntity, productLineEntity);
                    }).collect(Collectors.toList());

                    orderResponse.setterOrderDetailList(orderDetailDTOList);

                    return orderResponse;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageDTO approveOrder(Long orderId) {
        MessageDTO messageDTO;

        // 주문 유효성 검사
        orderRepository.findById(orderId)
                .filter(OrderEntity -> OrderEntity.getStatus() == Status.WAITING)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문이 존재하지 않거나 상태가 'WAITING'이 아니어서 처리할 수 없습니다."));

        orderSellerRepository.approveOrder(orderId);
        messageDTO = new MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 승인 되었습니다.");

        return messageDTO;
    }

    @Transactional
    public MessageDTO cancelOrder(Long orderId) {
        MessageDTO messageDTO;

        // 주문 유효성 검사
        orderRepository.findById(orderId)
                .filter(Order -> Order.getStatus() == Status.WAITING)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문이 존재하지 않거나 상태가 'WAITING'이 아니어서 처리할 수 없습니다."));

        orderSellerRepository.cancelOrder(orderId);
        orderDetailService.restoreStockByOrder(orderId);
        messageDTO = new MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 취소 되었습니다.");

        return messageDTO;
    }
}

