package org.store.clothstar.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import org.store.clothstar.common.config.jwt.JwtUtil;
import org.store.clothstar.member.domain.Account;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.repository.AccountRepository;
import org.store.clothstar.member.repository.AddressRepository;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.member.service.AddressServiceImpl;
import org.store.clothstar.member.util.CreateObject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AddressControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AddressServiceImpl addressServiceImpl;

    @Autowired
    private JwtUtil jwtUtil;

    private static final String ADDRESS_URL = "/v1/members/addresses/";

    private Member member;
    private Long memberId;
    private String accessToken;

    @DisplayName("회원가입한 멤버아이디와, 인증에 필요한 access 토큰을 가져옵니다.")
    @BeforeEach
    public void getMemberId_getAccessToken() {
        Account account = accountRepository.save(CreateObject.getAccount());
        member = memberRepository.save(CreateObject.getMemberByCreateMemberRequestDTO(account.getAccountId()));
        memberId = member.getMemberId();
        accessToken = jwtUtil.createAccessToken(account);
    }

    @DisplayName("회원 배송지 저장 통합 테스트")
    @Test
    void saveMemberAddrTest() throws Exception {
        //given
        final String url = ADDRESS_URL + memberId;
        CreateAddressRequest createAddressRequest = getCreateAddressRequest();
        final String addressRequestBody = objectMapper.writeValueAsString(createAddressRequest);

        //when
        ResultActions actions = mockMvc.perform(post(url)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addressRequestBody)
        );

        //then
        actions.andExpect(status().isCreated())
                .andDo(print());
    }

    @DisplayName("회원 전체 주소 리스트 조회 테스트")
    @WithMockUser
    @Test
    void memberGetAllAddressTest() throws Exception {
        //given
        final String getMemberAddressURL = ADDRESS_URL + memberId;
        for (int i = 0; i < 5; i++) {
            addressServiceImpl.addrSave(memberId, getCreateAddressRequest("서울시 공릉동" + i));
        }

        //when
        ResultActions actions = mockMvc.perform(get(getMemberAddressURL)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$.length()").value(5));
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

    private CreateAddressRequest getCreateAddressRequest(String addressBasic) {
        final String receiverName = "현수";
        final String zipNo = "18292";
        final String addressDetail = "양지빌라";
        final String telNo = "019-1222-2311";
        final String deliveryRequest = "문앞에 놓고 가주세요";
        final boolean defaultAddress = false;

        CreateAddressRequest createAddressRequest = new CreateAddressRequest(
                receiverName, zipNo, addressBasic, addressDetail, telNo, deliveryRequest, defaultAddress
        );

        return createAddressRequest;
    }
}