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
import org.store.clothstar.common.redis.RedisUtil;
import org.store.clothstar.member.domain.MemberRole;
import org.store.clothstar.member.dto.request.*;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.member.util.CreateObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MemberAndSellerSignUpIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RedisUtil redisUtil;

    private static final String MEMBER_URL = "/v1/members";
    private static final String SELLER_URL = "/v1/sellers";
    private MemberEntity memberEntity;

    @DisplayName("회원가입 통합테스트")
    @WithMockUser
    @Test
    void signUpIntegrationTest() throws Exception {
        //회원가입 통합 테스트
        //given
        String email = "test@naver.com";
        String certifyNum = redisUtil.createdCertifyNum();
        redisUtil.createRedisData(email, certifyNum);

        CreateMemberRequest createMemberRequest = CreateObject.getCreateMemberRequest(email, certifyNum);
        final String requestBody = objectMapper.writeValueAsString(createMemberRequest);

        //when
        ResultActions actions = mockMvc.perform(post(MEMBER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        actions.andExpect(status().isCreated());
    }

    @DisplayName("회원가입시 인증번호 전송 통합테스트")
    @WithMockUser
    @Test
    void certifyNumEmailSendTest() throws Exception {
        //given
        CertifyNumRequest certifyNumRequest = new CertifyNumRequest("test@naver.com");
        final String url = "/v1/members/auth";
        final String requestBody = objectMapper.writeValueAsString(certifyNumRequest);

        //when
        ResultActions actions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        //then
        actions.andExpect(status().isOk()).andDo(print());
    }

    @DisplayName("판매자(Seller) 가입 통합테스트")
    @WithMockUser(roles = "SELLER")
    @Test
    void sellerSignUpIntegrationTest() throws Exception {
        //given
        memberEntity = memberRepository.save(CreateObject.getMemberEntityByCreateMemberRequestDTO());
        Long memberId = memberEntity.getMemberId();
        final String sellerUrl = SELLER_URL + "/" + memberId;
        CreateSellerRequest createSellerRequest = getCreateSellerRequest(memberId);
        final String sellerRequestBody = objectMapper.writeValueAsString(createSellerRequest);

        //when
        ResultActions sellerActions = mockMvc.perform(post(sellerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(sellerRequestBody));

        //then
        sellerActions.andDo(print()).andExpect(status().isCreated());
    }

    @DisplayName("전체 회원 조회에 대한 통합테스트")
    @WithMockUser(roles = "ADMIN")
    @Test
    void getAllMemberTest() throws Exception {
        //given 3명의 회원을 만든다.
        memberRepository.save(CreateObject.getCreateMemberRequest("test1@naver.com").toMemberEntity());
        memberRepository.save(CreateObject.getCreateMemberRequest("test2@naver.com").toMemberEntity());
        memberRepository.save(CreateObject.getCreateMemberRequest("test3@naver.com").toMemberEntity());

        //when
        ResultActions actions = mockMvc.perform(get(MEMBER_URL)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk());
        actions.andDo(print());
        actions.andExpect(jsonPath("$.length()").value(3));
    }

    @DisplayName("회원 상세 정보 조회에 대한 통합테스트 이다.")
    @WithMockUser
    @Test
    void getMemberTest() throws Exception {
        //given
        memberEntity = memberRepository.save(CreateObject.getMemberEntityByCreateMemberRequestDTO());
        Long memberId = memberEntity.getMemberId();
        String getMemberURL = MEMBER_URL + "/" + memberId;

        //when
        ResultActions actions = mockMvc.perform(get(getMemberURL)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(memberId));
    }

    @DisplayName("이메일이 이미 사용중인 이메일인지 확인하는 테스트")
    @Test
    void emailDuplicationCheckTest() throws Exception {
        //given
        memberEntity = memberRepository.save(CreateObject.getMemberEntityByCreateMemberRequestDTO());
        String emailDuplicationCheckURL = MEMBER_URL + "/email/" + memberEntity.getEmail();

        //when
        ResultActions actions = mockMvc.perform(get(emailDuplicationCheckURL)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$.message").value("이미 사용중인 이메일 입니다."));
    }

    @DisplayName("회원 이름과 권한을 수정하는 테스트")
    @WithMockUser
    @Test
    void modifyName_modifyAuth_MemberTest() throws Exception {
        //given
        memberEntity = memberRepository.save(CreateObject.getMemberEntityByCreateMemberRequestDTO());
        Long memberId = memberEntity.getMemberId();
        String modifyMemberURL = MEMBER_URL + "/" + memberId;
        ModifyMemberRequest modifyMemberRequest = ModifyMemberRequest.builder()
                .name("관리자")
                .role(MemberRole.ADMIN)
                .build();

        final String modifyMemberRequestBody = objectMapper.writeValueAsString(modifyMemberRequest);

        //when
        ResultActions actions = mockMvc.perform(put(modifyMemberURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(modifyMemberRequestBody));

        //then
        //이름과 권한이 바꼈는지 확인
        actions.andExpect(status().isOk());
        MemberEntity memberEntity = memberRepository.findById(memberId).get();
        assertThat(memberEntity.getName()).isEqualTo("관리자");
        assertThat(memberEntity.getRole()).isEqualTo(MemberRole.ADMIN);
    }

    @DisplayName("회원 비밀번호 수정 통합 테스트")
    @WithMockUser
    @Test
    void modifyPasswordMemberTest() throws Exception {
        //given
        memberEntity = memberRepository.save(CreateObject.getMemberEntityByCreateMemberRequestDTO());
        final String originalPassword = memberEntity.getPassword();
        final String modifyPasswordMemberURL = MEMBER_URL + "/" + memberEntity.getMemberId();
        ModifyPasswordRequest modifyPasswordRequest = ModifyPasswordRequest.builder()
                .password("modified123")
                .build();

        final String modifyPasswordRequestBody = objectMapper.writeValueAsString(modifyPasswordRequest);

        //when
        ResultActions actions = mockMvc.perform(patch(modifyPasswordMemberURL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(modifyPasswordRequestBody));

        //then
        actions.andExpect(status().isOk());
        MemberEntity updatedMember = memberRepository.findById(memberEntity.getMemberId()).get();
        assertThat(originalPassword).isNotEqualTo(updatedMember.getPassword());
    }

    @DisplayName("한명 회원을 삭제요청 해서 deleteAt 필드가 현재시간으로 업데이트 된다.")
    @WithMockUser
    @Test
    void deleteMemberTest() throws Exception {
        //given
        //회원가입을 한 후 삭제 필드는 null이다.
        memberEntity = memberRepository.save(CreateObject.getMemberEntityByCreateMemberRequestDTO());
        Long memberId = memberEntity.getMemberId();
        assertThat(memberEntity.getDeletedAt()).isNull();
        String deleteURL = MEMBER_URL + "/" + memberId;

        //when
        ResultActions actions = mockMvc.perform(delete(deleteURL)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        MemberEntity deletedMember = memberRepository.findById(memberId).get();
        assertThat(deletedMember.getDeletedAt()).isNotNull();
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