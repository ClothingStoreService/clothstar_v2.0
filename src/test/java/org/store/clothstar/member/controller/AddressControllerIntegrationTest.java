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
import org.springframework.transaction.annotation.Transactional;
import org.store.clothstar.common.config.jwt.JwtUtil;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.dto.request.CreateAddressRequest;
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.entity.AddressEntity;
import org.store.clothstar.member.repository.AddressJpaRepository;
import org.store.clothstar.member.repository.MemberJpaRepositoryAdapter;
import org.store.clothstar.member.service.MemberSignupJpaServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private MemberSignupJpaServiceImpl memberSignupJpaService;

    @Autowired
    private MemberJpaRepositoryAdapter memberJpaRepositoryAdapter;

    @Autowired
    private AddressJpaRepository addressJpaRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private static final String ADDRESS_URL = "/v1/members/address/";
    private Long memberId;
    private String accessToken;
    private Member member;

    @DisplayName("회원가입한 멤버아이디와, 인증에 필요한 access 토큰을 가져옵니다.")
    @BeforeEach
    public void getMemberId_getAccessToken() {
        memberId = memberSignupJpaService.signUp(getCreateMemberRequest());
        member = memberJpaRepositoryAdapter.findById(memberId).get();
        accessToken = jwtUtil.createAccessToken(member);
    }

    @DisplayName("회원 배송지 저장 통합 테스트")
    @Test
    void saveMemberAddrTest() throws Exception {
        //given
        final String url = ADDRESS_URL + memberId;
        CreateAddressRequest createAddressRequest = getCreateAddressRequest();
        final String addressRequestBody = objectMapper.writeValueAsString(createAddressRequest);

        //when
        ResultActions result = mockMvc.perform(post(url)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addressRequestBody)
        );

        //then
        result.andExpect(status().isCreated())
                .andDo(print());

        //Address를 조회해서 memberId가 잘 들어갔는지 확인
        String responseBody = result.andReturn().getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long addressId = jsonNode.get("id").asLong();

        AddressEntity addressEntity = addressJpaRepository.findById(addressId).get();
        System.out.println("addressEntity.toString() " + addressEntity.toString());
        Assertions.assertThat(addressEntity.getAddressId()).isEqualTo(addressId);
        Assertions.assertThat(addressEntity.getMember().getMemberId()).isEqualTo(memberId);
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
}