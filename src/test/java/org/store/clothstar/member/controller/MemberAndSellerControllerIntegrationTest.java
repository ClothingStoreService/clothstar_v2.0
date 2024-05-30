package org.store.clothstar.member.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.CreateSellerRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("db-dev")
@Transactional
class MemberAndSellerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String MEMBER_SIGN_UP_URL = "/v1/members";
    private static final String SELLER_SIGN_UP_URL = "/v1/sellers/";

    @DisplayName("회원가입, 판매자 신청 통합 테스트")
    @Test
    void signUpAndSellerTest() throws Exception {
        //회원가입 통합 테스트
        //given
        CreateMemberRequest createMemberRequest = getCreateMemberRequest();
        final String requestBody = objectMapper.writeValueAsString(createMemberRequest);

        //when
        ResultActions actions = mockMvc.perform(post(MEMBER_SIGN_UP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        actions.andExpect(status().isCreated())
                .andDo(print());

        String responseBody = actions.andReturn().getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long memberId = jsonNode.get("id").asLong();

        //회원가입해서 받은 memberId로 판매자 신청 테스트
        //given
        final String sellerUrl = SELLER_SIGN_UP_URL + memberId;
        CreateSellerRequest createSellerRequest = getCreateSellerRequest(memberId);
        final String sellerRequestBody = objectMapper.writeValueAsString(createSellerRequest);

        //when
        ResultActions sellerActions = mockMvc.perform(post(sellerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(sellerRequestBody));

        //then
        Assertions.assertThat(memberId).isGreaterThan(0);
        sellerActions.andDo(print())
                .andExpect(status().isCreated());
    }

    private CreateMemberRequest getCreateMemberRequest() {
        String email = "test11@naver.com";
        String password = "testl122sff";
        String name = "name";
        String telNo = "010-1234-1245";

        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
                email, password, name, telNo
        );

        return createMemberRequest;
    }

    private CreateSellerRequest getCreateSellerRequest(Long memberId) {
        String brandName = "나이키";
        String bizNo = "102-13-13122";

        CreateSellerRequest createSellerRequest = new CreateSellerRequest(
                brandName, bizNo
        );

        return createSellerRequest;
    }
}