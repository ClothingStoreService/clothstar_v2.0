package org.store.clothstar.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.repository.MemberJpaRepositoryAdapter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SellerControllerValidationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MemberJpaRepositoryAdapter memberJpaRepository;

    private static final String SELLER_SIGN_UP_URL = "/v1/sellers/1";

    @DisplayName("판매자 가입시 브랜드명은 필수 값이다.")
    @WithMockUser(username = "현수", roles = "USER")
    @Test
    void sellerSignUp_brandNameValidationTest() throws Exception {
        //given
        CreateSellerRequest createSellerRequest = CreateSellerRequest.builder()
                .brandName(null)
                .bizNo("212-1243-12345")
                .build();

        final String requestBody = objectMapper.writeValueAsString(createSellerRequest);

        //when
        ResultActions actions = mockMvc.perform(post(SELLER_SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        actions.andExpect(status().is4xxClientError());
        actions.andExpect(jsonPath("$.errorMap.brandName").value("브랜드 이름은 필수 입력값 입니다."));
    }

    @DisplayName("판매자 가입시 사업자 번호 양식을 체크한다.")
    @WithMockUser(username = "현수", roles = "USER")
    @Test
    void sellerSignUp_bizNoValidationTest() throws Exception {
        //given
        CreateSellerRequest createSellerRequest = CreateSellerRequest.builder()
                .brandName("나이키")
                .bizNo("212-")
                .build();

        final String requestBody = objectMapper.writeValueAsString(createSellerRequest);

        //when
        ResultActions actions = mockMvc.perform(post(SELLER_SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        actions.andExpect(status().is4xxClientError());
        actions.andExpect(jsonPath("$.errorMap.bizNo").value("유효하지 않은 사업자 번호 형식입니다."));
    }
}