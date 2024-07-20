package org.store.clothstar.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.store.clothstar.category.dto.request.CreateCategoryRequest;
import org.store.clothstar.category.entity.CategoryEntity;
import org.store.clothstar.category.repository.CategoryJpaRepository;
import org.store.clothstar.common.dto.SaveResponseDTO;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.repository.AddressRepository;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.member.repository.SellerRepository;
import org.store.clothstar.member.util.CreateObject;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;
import org.store.clothstar.order.domain.type.PaymentMethod;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.dto.request.AddOrderDetailRequest;
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.dto.request.OrderRequestWrapper;
import org.store.clothstar.order.repository.order.OrderDetailRepository;
import org.store.clothstar.order.repository.order.OrderUserRepository;
import org.store.clothstar.order.service.OrderDetailService;
import org.store.clothstar.order.service.OrderService;
import org.store.clothstar.order.util.CreateOrder;
import org.store.clothstar.product.dto.request.CreateProductRequest;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.repository.ProductJPARepository;
import org.store.clothstar.productLine.domain.type.ProductLineStatus;
import org.store.clothstar.productLine.dto.request.CreateProductLineRequest;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class OrderUserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderUserRepository orderUserRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProductJPARepository productJPARepository;

    @Autowired
    private ProductLineJPARepository productLineJPARepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private SellerRepository sellerRepository;

    private static final String ORDER_URL = "/v1/orders";
    private Order order;

    @DisplayName("단일 주문 조회 통합테스트")
    @Test
    public void testGetOrder() throws Exception {
        //given
        Member member = memberRepository.save(CreateObject.getMemberByCreateMemberRequestDTO());
        Address address = addressRepository.save(CreateOrder.getCreateAddressRequest().toAddress(member));
        CategoryEntity category = categoryJpaRepository.save(CreateOrder.getCreateCategoryRequest().toCategoryEntity());
        Seller seller = sellerRepository.save(new Seller(CreateOrder.getCreateSellerRequest(),member));
        ProductLineEntity productLine = productLineJPARepository.save(CreateOrder.getCreateProductLineRequest().toProductLineEntity(seller,category));
        ProductEntity product = productJPARepository.save(CreateOrder.getCreateProductRequest(productLine.getProductLineId()).toProductEntity(productLine));
        OrderDetail orderDetail = orderDetailRepository.save(CreateOrder.getCreateOrderDetailRequest(productLine.getProductLineId(),product.getProductId()).toOrderDetail(order,productLine,product));

        order = orderUserRepository.save(CreateOrder.getCreateOrderRequest(member.getMemberId(),address.getAddressId()).toOrder(member,address));
        order.addOrderDetail(orderDetail);

        Long orderId = order.getOrderId();
        String getOrderURL = ORDER_URL + "/" + orderId;

        //when
        ResultActions actions = mockMvc.perform(get(getOrderURL)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId));
    }

    @DisplayName("전체 주문 조회 offset 페이징 통합테스트")
    @Test
    public void testGetAllOrderOffsetPaging() throws Exception {
        //given
        // 필요한 데이터 생성 (여러 주문을 추가)
        createOrders(110);

        //when
        ResultActions actions = mockMvc.perform(get(ORDER_URL + "/offset")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(10));
    }

    @DisplayName("전체 주문 조회 slice 페이징 통합테스트")
    @Test
    public void testGetAllOrderSlicePaging() throws Exception {
        //given
        // 필요한 데이터 생성 (여러 주문을 추가)
        createOrders(110);

        //when
        ResultActions actions = mockMvc.perform(get(ORDER_URL + "/slice")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(10));
    }

    private OrderRequestWrapper getOrderRequestWrapper(CreateOrderRequest createOrderRequest, CreateOrderDetailRequest createOrderDetailRequest) {

        OrderRequestWrapper orderRequestWrapper = new OrderRequestWrapper(
                createOrderRequest,createOrderDetailRequest
        );

        return orderRequestWrapper;
    }

    @DisplayName("구매 확정 통합테스트")
    @Test
    public void testConfirmOrder() throws Exception {
        //given
        createOrderWithStatus(Status.DELIVERED);

        Long orderId = order.getOrderId();
        String confirmOrderURL = ORDER_URL + "/" + orderId + "/confirm";

        //when
        ResultActions actions = mockMvc.perform(patch(confirmOrderURL)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("주문이 정상적으로 구매 확정 되었습니다."));

        // 데이터베이스에서 주문 상태 조회하여 검증
        Order savedOrder = orderUserRepository.findById(order.getOrderId()).orElse(null);
        assertNotNull(savedOrder);
        assertEquals(Status.CONFIRM, savedOrder.getStatus());
    }

    @DisplayName("주문 취소 통합테스트")
    @Test
    public void testCancelOrder() throws Exception {
        //given
        createOrderWithStatus(Status.WAITING);

        Long orderId = order.getOrderId();
        String cancelOrderURL = ORDER_URL + "/" + orderId + "/cancel";

        //when
        ResultActions actions = mockMvc.perform(patch(cancelOrderURL)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("주문이 정상적으로 취소되었습니다."));

        // 데이터베이스에서 주문 상태 조회하여 검증
        Order savedOrder = orderUserRepository.findById(order.getOrderId()).orElse(null);
        assertNotNull(savedOrder);
        assertEquals(Status.CANCEL, savedOrder.getStatus());
    }

    @DisplayName("주문 삭제 통합테스트")
    @Test
    public void testDeleteOrder() throws Exception {
        //given
        createOrderWithStatus(Status.WAITING);

        Long orderId = order.getOrderId();
        String deleteOrderURL = ORDER_URL + "/" + orderId;

        //when
        ResultActions actions = mockMvc.perform(delete(deleteOrderURL)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("주문이 정상적으로 삭제되었습니다."));

        // 데이터베이스에서 주문 조회하여 삭제 여부 검증
        Order savedOrder = orderUserRepository.findById(order.getOrderId()).orElse(null);
        assertNotNull(savedOrder);
        assertNotNull(savedOrder.getDeletedAt());
    }

    public void createOrders(int count) {
        for (int i = 100; i < count; i++) {
            Member member = memberRepository.save(CreateObject.getMemberByCreateMemberRequestDTO(i));
            Address address = addressRepository.save(CreateOrder.getCreateAddressRequest().toAddress(member));
            CategoryEntity category = categoryJpaRepository.save(CreateOrder.getCreateCategoryRequest(i).toCategoryEntity()); // 수정된 부분
            Seller seller = sellerRepository.save(new Seller(CreateOrder.getCreateSellerRequest(i), member));
            ProductLineEntity productLine = productLineJPARepository.save(CreateOrder.getCreateProductLineRequest(category.getCategoryId()).toProductLineEntity(seller, category)); // 수정된 부분
            ProductEntity product = productJPARepository.save(CreateOrder.getCreateProductRequest(productLine.getProductLineId()).toProductEntity(productLine)); // 수정된 부분
            Order order = orderUserRepository.save(CreateOrder.getCreateOrderRequest(member.getMemberId(), address.getAddressId()).toOrder(member, address)); // 수정된 부분
            OrderDetail orderDetail = orderDetailRepository.save(CreateOrder.getCreateOrderDetailRequest(productLine.getProductLineId(), product.getProductId()).toOrderDetail(order, productLine, product)); // 수정된 부분

            order.addOrderDetail(orderDetail);
        }
    }

    public void createOrderWithStatus(Status status) {
        Member member = memberRepository.save(CreateObject.getMemberByCreateMemberRequestDTO());
        member = memberRepository.findById(member.getMemberId()).get();
//        entityManager.merge(member);
        Address address = addressRepository.save(CreateOrder.getCreateAddressRequest().toAddress(member));
        CategoryEntity category = categoryJpaRepository.save(CreateOrder.getCreateCategoryRequest().toCategoryEntity());
        Seller seller = sellerRepository.save(new Seller(CreateOrder.getCreateSellerRequest(),member));
        ProductLineEntity productLine = productLineJPARepository.save(CreateOrder.getCreateProductLineRequest().toProductLineEntity(seller,category));
        ProductEntity product = productJPARepository.save(CreateOrder.getCreateProductRequest(productLine.getProductLineId()).toProductEntity(productLine));
        order = orderUserRepository.save(CreateOrder.getCreateOrderRequest(member.getMemberId(),address.getAddressId()).toOrder(member,address));
        OrderDetail orderDetail = orderDetailRepository.save(CreateOrder.getCreateOrderDetailRequest(productLine.getProductLineId(),product.getProductId()).toOrderDetail(order,productLine,product));
        productLine.addProduct(product);


        order.setterStatus(status);
        order.addOrderDetail(orderDetail);
//        entityManager.flush();
//        entityManager.clear();
    }
}

