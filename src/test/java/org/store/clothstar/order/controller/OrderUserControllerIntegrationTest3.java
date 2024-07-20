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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class OrderUserControllerIntegrationTest3 {
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


    @DisplayName("주문 생성 통합테스트")
    @Test
    public void testSaveOrder() throws Exception {
        //given
        Member member = memberRepository.save(CreateObject.getCreateMemberRequest().toMember());
        Address address = addressRepository.save(CreateOrder.getCreateAddressRequest().toAddress(member));
        CreateOrderRequest createOrderRequest = CreateOrder.getCreateOrderRequest(member.getMemberId(),address.getAddressId());
        CategoryEntity category = categoryJpaRepository.save(CreateOrder.getCreateCategoryRequest().toCategoryEntity());
        Seller seller = sellerRepository.save(new Seller(CreateOrder.getCreateSellerRequest(),member));
        ProductLineEntity productLine = productLineJPARepository.save(CreateOrder.getCreateProductLineRequest().toProductLineEntity(seller,category));
        ProductEntity product = productJPARepository.save(CreateOrder.getCreateProductRequest(productLine.getProductLineId()).toProductEntity(productLine));
        CreateOrderDetailRequest createOrderDetailRequest = CreateOrder.getCreateOrderDetailRequest(productLine.getProductLineId(),product.getProductId());
        productLine.addProduct(product);

        OrderRequestWrapper orderRequestWrapper = getOrderRequestWrapper(createOrderRequest,createOrderDetailRequest);

        //when
        System.out.println("productLineId" + productLine.getProductLineId());
        System.out.println("productId" + product.getProductId());
        ResultActions actions = mockMvc.perform(post(ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequestWrapper)));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("주문이 정상적으로 생성되었습니다."));
    }

    private OrderRequestWrapper getOrderRequestWrapper(CreateOrderRequest createOrderRequest, CreateOrderDetailRequest createOrderDetailRequest) {

        OrderRequestWrapper orderRequestWrapper = new OrderRequestWrapper(
                createOrderRequest,createOrderDetailRequest
        );

        return orderRequestWrapper;
    }
}

