package org.store.clothstar.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.service.AddressService;
import org.store.clothstar.member.service.MemberService;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.entity.OrderEntity;
import org.store.clothstar.order.repository.order.OrderRepository;
import org.store.clothstar.order.type.Status;
import org.store.clothstar.orderDetail.dto.OrderDetailDTO;
import org.store.clothstar.orderDetail.entity.OrderDetailEntity;
import org.store.clothstar.orderDetail.repository.OrderDetailRepository;
import org.store.clothstar.orderDetail.service.OrderDetailService;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.service.ProductService;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.service.ProductLineService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final AddressService addressService;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;
    private final ProductLineService productLineService;

    public OrderService(
            OrderRepository orderRepository
            , MemberService memberService, AddressService addressService
            , OrderDetailService orderDetailService, OrderDetailRepository orderDetailRepository,
            ProductService productService, ProductLineService productLineService) {
        this.orderRepository = orderRepository;
        this.memberService = memberService;
        this.addressService = addressService;
        this.orderDetailRepository = orderDetailRepository;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
        this.productLineService = productLineService;
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다"));

        Member member = memberService.getMemberByMemberId(orderEntity.getMemberId());
        Address address = addressService.getAddressById(orderEntity.getAddressId());

        // 주문 Response 객체 생성
        OrderResponse orderResponse = OrderResponse.from(orderEntity,member,address);

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

//        return orderRepository.findOrderWithDetails(orderId);
    }

//    public Page<OrderResponse> getAllOrderOffsetPaging(Pageable pageable) {
//        return orderRepository.findAllOffsetPaging(pageable);
//    }
//
//    public Slice<OrderResponse> getAllOrderSlicePaging(Pageable pageable) {
//        return orderRepository.findAllSlicePaging(pageable);
//    }

    @Transactional
    public Long saveOrder(CreateOrderRequest createOrderRequest) {

        Member member = memberService.getMemberByMemberId(createOrderRequest.getMemberId());
        Address address = addressService.getAddressById(createOrderRequest.getAddressId());

        OrderEntity orderEntity = createOrderRequest.toOrderEntity(member, address);
        orderRepository.save(orderEntity);

        return orderEntity.getOrderId();
    }

    @Transactional
    public void deliveredToConfirmOrder(Long orderId) {

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 정보를 찾을 수 없습니다."));

        if (orderEntity.getStatus() != Status.DELIVERED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 상태가 '배송완료'가 아니기 때문에 주문확정이 불가능합니다.");
        }

        orderRepository.deliveredToConfirmOrder(orderId);
    }

    @Transactional
    public void updateDeleteAt(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 번호를 찾을 수 없습니다."));

        if(orderEntity.getDeletedAt() != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 삭제된 주문입니다.");
        }

        List<OrderDetailEntity> orderDetailList = orderDetailRepository.findOrderDetailListByOrderId(orderId);
        orderDetailList.forEach(OrderDetailEntity::updateDeletedAt);

        orderEntity.updateDeletedAt();
    }
}