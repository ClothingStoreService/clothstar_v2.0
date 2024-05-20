package org.store.clothstar.member.controller;

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
import org.store.clothstar.member.dto.request.CreateAddressRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@Transactional
class AddressControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    final Long memberId = 1L;

    @DisplayName("회원 배송지 저장 통합 테스트")
    @Test
    void saveMemberAddrTest() throws Exception {
        //given
        final String url = "/v1/members/" + memberId + "/address";
        CreateAddressRequest createAddressRequest = getCreateAddressRequest();
        final String requestBody = objectMapper.writeValueAsString(createAddressRequest);

        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        result.andExpect(status().isCreated());
    }

    private CreateAddressRequest getCreateAddressRequest() {
        final String receiverName = "현수";
        final String zipNo = "18292";
        final String addressBasic = "서울시 노원구 공릉동";
        final String addressDetail = "양지빌라";
        final String telNo = "019-1222-2311";
        final String deliveryRequest = "문앞에 놓고 가주세요";
        final boolean defaultAddress = true;

        CreateAddressRequest createAddressRequest = new CreateAddressRequest(
                receiverName, zipNo, addressBasic, addressDetail, telNo, deliveryRequest, defaultAddress
        );
        return createAddressRequest;
    }
}