package org.store.clothstar.order.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.service.AddressService;
import org.store.clothstar.member.service.MemberService;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.domain.vo.OrderDetailDTO;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.repository.order.OrderUserRepository;
import org.store.clothstar.order.repository.orderSeller.OrderSellerRepository;
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
    private final OrderUserRepository orderUserRepository;
    private final OrderDetailService orderDetailService;
    private final MemberService memberService;
    private final AddressService addressService;
    private final ProductService productService;
    private final ProductLineService productLineService;

    public OrderSellerService(
            OrderSellerRepository orderSellerRepository
            , OrderUserRepository orderUserRepository
            , OrderDetailService orderDetailService
            , MemberService memberService, AddressService addressService
            , ProductService productService, ProductLineService productLineService
    ) {
        this.orderSellerRepository = orderSellerRepository;
        this.orderUserRepository = orderUserRepository;
        this.orderDetailService=orderDetailService;
        this.memberService = memberService;
        this.addressService = addressService;
        this.productService = productService;
        this.productLineService = productLineService;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getWaitingOrder() {
        List<Order> waitingOrders = orderSellerRepository.findWaitingOrders();

        List<Order> filteredOrders = waitingOrders.stream()
                .toList();

        return filteredOrders.stream()
                .map(order -> {
                    Member member = memberService.getMemberByMemberId(order.getMemberId());
                    Address address = addressService.getAddressById(order.getAddressId());
                    OrderResponse orderResponse = OrderResponse.from(order, member, address);

                    List<OrderDetail> orderDetails = order.getOrderDetails().stream()
                            .filter(orderDetail -> orderDetail.getDeletedAt() == null)
                            .toList();
                    List<Long> productIds = orderDetails.stream().map(OrderDetail::getProductId).collect(Collectors.toList());
                    List<Long> productLineIds = orderDetails.stream().map(OrderDetail::getProductLineId).collect(Collectors.toList());

                    List<ProductEntity> products = productService.findByIdIn(productIds);
                    List<ProductLineEntity> productLines = productLineService.findByIdIn(productLineIds);

                    Map<Long, ProductEntity> productMap = products.stream().collect(Collectors.toMap(ProductEntity::getId, product -> product));
                    Map<Long, ProductLineEntity> productLineMap = productLines.stream().collect(Collectors.toMap(ProductLineEntity::getId, productLine -> productLine));

                    List<OrderDetailDTO> orderDetailDTOList = orderDetails.stream().map(orderDetail -> {
                        ProductEntity productEntity = productMap.get(orderDetail.getProductId());
                        ProductLineEntity productLineEntity = productLineMap.get(orderDetail.getProductLineId());
                        return OrderDetailDTO.from(orderDetail, productEntity, productLineEntity);
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
        Order order = orderUserRepository.findById(orderId)
                .filter(Order -> Order.getStatus() == Status.WAITING)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문이 존재하지 않거나 상태가 'WAITING'이 아니어서 처리할 수 없습니다."));

        order.setterStatus(Status.APPROVE);
        orderSellerRepository.save(order);
//        orderSellerRepository.approveOrder(orderId);
        messageDTO = new MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 승인 되었습니다.");

        return messageDTO;
    }

    @Transactional
    public MessageDTO cancelOrder(Long orderId) {
        MessageDTO messageDTO;

        // 주문 유효성 검사
        Order order = orderUserRepository.findById(orderId)
                .filter(Order -> Order.getStatus() == Status.WAITING)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문이 존재하지 않거나 상태가 'WAITING'이 아니어서 처리할 수 없습니다."));

        order.setterStatus(Status.CANCEL);
        orderSellerRepository.save(order);
        orderDetailService.restoreStockByOrder(orderId);
        messageDTO = new MessageDTO(HttpStatus.OK.value(), "주문이 정상적으로 취소 되었습니다.");

        return messageDTO;
    }
}

