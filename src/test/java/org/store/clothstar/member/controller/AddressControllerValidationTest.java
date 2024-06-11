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
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.repository.MemberJpaRepositoryAdapter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AddressControllerValidationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MemberJpaRepositoryAdapter memberJpaRepository;

    private static final String ADDRESS_ADD_URL = "/v1/members/address/1";

    @DisplayName("주소 추가시 받는사람은 필수 값이다.")
    @WithMockUser(username = "현수", roles = "USER")
    @Test
    void addressAdd_receiverNameValidationTest() throws Exception {
        //given
        CreateAddressRequest createAddressRequest = CreateAddressRequest.builder()
                .receiverName(null)
                .zipNo("12233")
                .addressBasic("서울시 노원구 공릉동")
                .addressDetail("101동")
                .telNo("010-2122-2123")
                .build();

        final String requestBody = objectMapper.writeValueAsString(createAddressRequest);

        //when
        ResultActions actions = mockMvc.perform(post(ADDRESS_ADD_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        actions.andExpect(status().is4xxClientError());
        actions.andExpect(jsonPath("$.errorMap.receiverName").value("받는 사람 이름은 비어 있을 수 없습니다."));
    }

    @DisplayName("주소 추가시 우편번호는 필수 값이다.")
    @WithMockUser(username = "현수", roles = "USER")
    @Test
    void addressAdd_zipNoValidationTest() throws Exception {
        //given
        CreateAddressRequest createAddressRequest = CreateAddressRequest.builder()
                .receiverName("현수")
                .zipNo(null)
                .addressBasic("서울시 노원구 공릉동")
                .addressDetail("101동")
                .telNo("010-2122-2123")
                .build();

        final String requestBody = objectMapper.writeValueAsString(createAddressRequest);

        //when
        ResultActions actions = mockMvc.perform(post(ADDRESS_ADD_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        actions.andExpect(status().is4xxClientError());
        actions.andExpect(jsonPath("$.errorMap.zipNo").value("우편번호는 비어 있을 수 없습니다."));
    }

    @DisplayName("주소 추가시 기본주소는 필수 값이다.")
    @WithMockUser(username = "현수", roles = "USER")
    @Test
    void addressAdd_addressBasicValidationTest() throws Exception {
        //given
        CreateAddressRequest createAddressRequest = CreateAddressRequest.builder()
                .receiverName("현수")
                .zipNo("81022")
                .addressBasic(null)
                .addressDetail("101동")
                .telNo("010-2122-2123")
                .build();

        final String requestBody = objectMapper.writeValueAsString(createAddressRequest);

        //when
        ResultActions actions = mockMvc.perform(post(ADDRESS_ADD_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        actions.andExpect(status().is4xxClientError());
        actions.andExpect(jsonPath("$.errorMap.addressBasic").value("기본 주소는 비어 있을 수 없습니다."));
    }

    @DisplayName("주소 추가시 전화번호 양식을 지켜야 한다.")
    @WithMockUser(username = "현수", roles = "USER")
    @Test
    void addressAdd_telNoValidationTest() throws Exception {
        //given
        CreateAddressRequest createAddressRequest = CreateAddressRequest.builder()
                .receiverName("현수")
                .zipNo("81022")
                .addressBasic("서울시 노원구 공릉동")
                .addressDetail("101동")
                .telNo("010-")
                .build();

        final String requestBody = objectMapper.writeValueAsString(createAddressRequest);

        //when
        ResultActions actions = mockMvc.perform(post(ADDRESS_ADD_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        actions.andExpect(status().is4xxClientError());
        actions.andExpect(jsonPath("$.errorMap.telNo").value("유효하지 않은 전화번호 형식입니다."));
    }

}