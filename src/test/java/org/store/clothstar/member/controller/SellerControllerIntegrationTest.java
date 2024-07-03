package org.store.clothstar.member.controller;

import jakarta.transaction.Transactional;
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
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.dto.request.CreateSellerRequest;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.member.service.MemberServiceImpl;
import org.store.clothstar.member.service.SellerServiceImpl;
import org.store.clothstar.member.util.CreateObject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SellerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SellerServiceImpl sellerServiceImpl;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    MemberServiceImpl memberServiceImpl;

    private static final String SELLER_URL = "/v1/sellers";

    MemberEntity memberEntity;
    private Long memberId;
    private final String brandName = "나이키";
    private final String bizNo = "102-13-13122";

    @DisplayName("판매자 상세 정보 조회 테스트")
    @WithMockUser(roles = "SELLER")
    @Test
    void getSellerTest() throws Exception {
        //given
        memberEntity = memberRepository.save(CreateObject.getMemberEntityByCreateMemberRequestDTO());
        memberId = memberEntity.getMemberId();
        Long sellerId = sellerServiceImpl.sellerSave(memberId, getCreateSellerRequest());
        String sellerMemberIdURL = SELLER_URL + "/" + memberId;

        //when
        ResultActions actions = mockMvc.perform(get(sellerMemberIdURL)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$.memberId").value(sellerId));
        actions.andExpect(jsonPath("$.brandName").value(brandName));
        actions.andExpect(jsonPath("$.bizNo").value(bizNo));
    }


    private CreateMemberRequest getCreateMemberRequest(String email) {
        String password = "test1234";
        String name = "현수";
        String telNo = "010-1234-1244";
        String certifyNum = "123asdf";

        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
                email, password, name, telNo, certifyNum
        );

        return createMemberRequest;
    }

    private CreateSellerRequest getCreateSellerRequest() {
        CreateSellerRequest createSellerRequest = new CreateSellerRequest(
                brandName, bizNo
        );

        return createSellerRequest;
    }
}