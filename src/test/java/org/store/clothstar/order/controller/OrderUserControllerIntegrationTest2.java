package org.store.clothstar.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.store.clothstar.category.entity.CategoryEntity;
import org.store.clothstar.category.repository.CategoryJpaRepository;
import org.store.clothstar.common.dto.SaveResponseDTO;
import org.store.clothstar.member.domain.Address;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.Seller;
import org.store.clothstar.member.repository.AddressRepository;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.member.repository.SellerRepository;
import org.store.clothstar.member.util.CreateObject;
import org.store.clothstar.order.domain.Order;
import org.store.clothstar.order.domain.OrderDetail;
import org.store.clothstar.order.domain.type.Status;
import org.store.clothstar.order.dto.request.AddOrderDetailRequest;
import org.store.clothstar.order.dto.request.CreateOrderDetailRequest;
import org.store.clothstar.order.dto.request.CreateOrderRequest;
import org.store.clothstar.order.dto.request.OrderRequestWrapper;
import org.store.clothstar.order.repository.order.OrderDetailRepository;
import org.store.clothstar.order.repository.order.OrderUserRepository;
import org.store.clothstar.order.util.CreateOrder;
import org.store.clothstar.product.entity.ProductEntity;
import org.store.clothstar.product.repository.ProductJPARepository;
import org.store.clothstar.productLine.entity.ProductLineEntity;
import org.store.clothstar.productLine.repository.ProductLineJPARepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class OrderUserControllerIntegrationTest2 {
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

    @DisplayName("주문상세 추가 저장 통합테스트")
    @Test
    public void testAddOrderDetail() throws Exception {
        //give
        Member member = memberRepository.save(CreateObject.getMemberByCreateMemberRequestDTO());
        Address address = addressRepository.save(CreateOrder.getCreateAddressRequest().toAddress(member));
        CreateOrderRequest createOrderRequest = CreateOrder.getCreateOrderRequest(member.getMemberId(),address.getAddressId());
        order = orderUserRepository.save(CreateOrder.getCreateOrderRequest(member.getMemberId(),address.getAddressId()).toOrder(member,address));
        CategoryEntity category = categoryJpaRepository.save(CreateOrder.getCreateCategoryRequest().toCategoryEntity());
        Seller seller = sellerRepository.save(new Seller(CreateOrder.getCreateSellerRequest(),member));
        ProductLineEntity productLine = productLineJPARepository.save(CreateOrder.getCreateProductLineRequest().toProductLineEntity(seller,category));
        ProductEntity product = productJPARepository.save(CreateOrder.getCreateProductRequest(productLine.getProductLineId()).toProductEntity(productLine));
        System.out.println("productLine: "+productLine.getProductLineId());
        OrderDetail orderDetail = orderDetailRepository.save(CreateOrder.getCreateOrderDetailRequest(productLine.getProductLineId(),product.getProductId()).toOrderDetail(order,productLine,product));

        order.setterStatus(Status.WAITING);
        order.addOrderDetail(orderDetail);

        productLine.addProduct(product);

        AddOrderDetailRequest addOrderDetailRequest = CreateOrder.getAddOrderDetailRequest(order.getOrderId(),productLine.getProductLineId(),product.getProductId());

        //when
        System.out.println("orderId="+order.getOrderId());
        ResultActions actions = mockMvc.perform(post("/v1/orderdetails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addOrderDetailRequest)));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("주문상세가 정상적으로 생성되었습니다."));

        // 데이터베이스에서 주문 상세 조회하여 검증
        Long orderDetailId = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), SaveResponseDTO.class).getId();
        assertNotNull(orderDetailRepository.findById(orderDetailId).orElse(null));
    }
}

