package org.store.clothstar.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.service.AddressService;
import org.store.clothstar.member.service.MemberService;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.domain.vo.OrderDetailDTO;
import org.store.clothstar.order.dto.reponse.OrderResponse;
import org.store.clothstar.order.dto.request.OrderRequestWrapper;
import org.store.clothstar.order.repository.order.OrderDetailRepository;
import org.store.clothstar.order.repository.order.OrderUserRepository;
import org.store.clothstar.order.service.OrderSave.OrderSaveFacade;
import org.store.clothstar.product.domain.Product;
import org.store.clothstar.product.service.ProductService;
import org.store.clothstar.productLine.domain.ProductLine;
import org.store.clothstar.productLine.service.ProductLineService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    private final OrderUserRepository orderUserRepository;
    private final MemberService memberService;
    private final AddressService addressService;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;
    private final ProductLineService productLineService;
    private final OrderSaveFacade orderSaveFacade;


    public OrderService(
            OrderUserRepository orderUserRepository
            , MemberService memberService, AddressService addressService
            , OrderDetailService orderDetailService, OrderDetailRepository orderDetailRepository
            , ProductService productService, ProductLineService productLineService
            , OrderSaveFacade orderSaveFacade
    ) {
        this.orderUserRepository = orderUserRepository;
        this.memberService = memberService;
        this.addressService = addressService;
        this.orderDetailRepository = orderDetailRepository;
        this.orderDetailService = orderDetailService;
        this.productService = productService;
        this.productLineService = productLineService;
        this.orderSaveFacade=orderSaveFacade;
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {
        Order order = orderUserRepository.findByOrderIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다"));

        Member member = memberService.getMemberByMemberId(order.getMemberId());
        Address address = addressService.getAddressById(order.getAddressId());

        OrderResponse orderResponse = OrderResponse.from(order,member,address);

        List<OrderDetail> orderDetails = order.getOrderDetails().stream()
                .filter(orderDetail -> orderDetail.getDeletedAt() == null)
                .toList();
        List<Long> productIds = orderDetails.stream().map(OrderDetail::getProductId).collect(Collectors.toList());
        List<Long> productLineIds = orderDetails.stream().map(OrderDetail::getProductLineId).collect(Collectors.toList());

        List<Product> products = productService.findByIdIn(productIds);
        List<ProductLine> productLines = productLineService.findByIdIn(productLineIds);

        Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, product -> product));
        Map<Long, ProductLine> productLineMap = productLines.stream().collect(Collectors.toMap(ProductLine::getId, productLine -> productLine));

        List<OrderDetailDTO> orderDetailDTOList = orderDetails.stream().map(orderDetail -> {
            Product product = productMap.get(orderDetail.getProductId());
            ProductLine productLine = productLineMap.get(orderDetail.getProductLineId());
            return OrderDetailDTO.from(orderDetail, product, productLine);
        }).collect(Collectors.toList());

        orderResponse.setterOrderDetailList(orderDetailDTOList);

        return orderResponse;
    }

    public Page<OrderResponse> getAllOrderOffsetPaging(Pageable pageable) {
        Page<Order> orderEntities = orderUserRepository.findAll(pageable);

        return orderEntities.map(order -> {
            Member member = memberService.getMemberByMemberId(order.getMemberId());
            Address address = addressService.getAddressById(order.getAddressId());
            OrderResponse orderResponse = OrderResponse.from(order, member, address);

            List<OrderDetail> orderDetails = order.getOrderDetails().stream()
                    .filter(orderDetail -> orderDetail.getDeletedAt() == null)
                    .toList();
            List<Long> productIds = orderDetails.stream().map(OrderDetail::getProductId).collect(Collectors.toList());
            List<Long> productLineIds = orderDetails.stream().map(OrderDetail::getProductLineId).collect(Collectors.toList());

            List<Product> products = productService.findByIdIn(productIds);
            List<ProductLine> productLines = productLineService.findByIdIn(productLineIds);

            Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, product -> product));
            Map<Long, ProductLine> productLineMap = productLines.stream().collect(Collectors.toMap(ProductLine::getId, productLine -> productLine));

            List<OrderDetailDTO> orderDetailDTOList = orderDetails.stream().map(orderDetail -> {
                Product product = productMap.get(orderDetail.getProductId());
                ProductLine productLine = productLineMap.get(orderDetail.getProductLineId());
                return OrderDetailDTO.from(orderDetail, product, productLine);
            }).collect(Collectors.toList());

            orderResponse.setterOrderDetailList(orderDetailDTOList);

            return orderResponse;
        });
    }

    public Slice<OrderResponse> getAllOrderSlicePaging(Pageable pageable) {
        Slice<Order> orderEntities = orderUserRepository.findAll(pageable);

        return orderEntities.map(order -> {
            Member member = memberService.getMemberByMemberId(order.getMemberId());
            Address address = addressService.getAddressById(order.getAddressId());
            OrderResponse orderResponse = OrderResponse.from(order, member, address);

            List<OrderDetail> orderDetails = order.getOrderDetails().stream()
                    .filter(orderDetail -> orderDetail.getDeletedAt() == null)
                    .toList();
            List<Long> productIds = orderDetails.stream().map(OrderDetail::getProductId).collect(Collectors.toList());
            List<Long> productLineIds = orderDetails.stream().map(OrderDetail::getProductLineId).collect(Collectors.toList());

            List<Product> products = productService.findByIdIn(productIds);
            List<ProductLine> productLines = productLineService.findByIdIn(productLineIds);

            Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, product -> product));
            Map<Long, ProductLine> productLineMap = productLines.stream().collect(Collectors.toMap(ProductLine::getId, productLine -> productLine));

            List<OrderDetailDTO> orderDetailDTOList = orderDetails.stream().map(orderDetail -> {
                Product product = productMap.get(orderDetail.getProductId());
                ProductLine productLine = productLineMap.get(orderDetail.getProductLineId());
                return OrderDetailDTO.from(orderDetail, product, productLine);
            }).collect(Collectors.toList());

            orderResponse.setterOrderDetailList(orderDetailDTOList);

            return orderResponse;
        });
    }

    @Transactional
    public Long saveOrder(OrderRequestWrapper orderRequestWrapper) {
        return orderSaveFacade.saveOrder(orderRequestWrapper);
    }

    @Transactional
    public void confirmOrder(Long orderId) {

        Order order = orderUserRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 정보를 찾을 수 없습니다."));

        if (order.getStatus() != Status.DELIVERED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 상태가 '배송완료'가 아니기 때문에 주문확정이 불가능합니다.");
        }

        order.setterStatus(Status.CONFIRM);
        orderUserRepository.save(order);
    }

    public void cancelOrder(Long orderId) {

        Order order = orderUserRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 정보를 찾을 수 없습니다."));

        if (order.getStatus() != Status.WAITING && order.getStatus() != Status.APPROVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'승인대기' 또는 '주문승인' 상태가 아니기 때문에 주문을 취소할 수 없습니다.");
        }

        order.setterStatus(Status.CANCEL);
        orderUserRepository.save(order);

        orderDetailService.restoreStockByOrder(orderId);
    }

    @Transactional
    public void updateDeleteAt(Long orderId) {
        Order order = orderUserRepository.findById(orderId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 번호를 찾을 수 없습니다."));

        if(order.getDeletedAt() != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 삭제된 주문입니다.");
        }

        List<OrderDetail> orderDetailList = orderDetailRepository.findOrderDetailListByOrderId(orderId);
        orderDetailList.forEach(OrderDetail::updateDeletedAt);

        order.updateDeletedAt();
    }

}