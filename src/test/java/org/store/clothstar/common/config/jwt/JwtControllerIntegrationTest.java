package org.store.clothstar.common.config.jwt;

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
import org.store.clothstar.member.dto.request.CreateMemberRequest;
import org.store.clothstar.member.entity.MemberEntity;
import org.store.clothstar.member.repository.MemberRepository;
import org.store.clothstar.member.service.MemberService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class JwtControllerIntegrationTest {
    @Autowired
    private JwtController jwtController;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    private Long memberId;
    private MemberEntity memberEntity;

    @DisplayName("회원가입한 멤버아이디와, 인증에 필요한 access 토큰을 가져옵니다.")
    @BeforeEach
    public void getMemberId_getAccessToken() {
        memberId = memberService.signUp(getCreateMemberRequest());
        memberEntity = memberRepository.findById(memberId).get();
    }

    @DisplayName("refresh 토큰으로 access 토큰을 가져온다.")
    @Test
    void accessToken_reissue_by_RefreshToken() throws Exception {
        //given
        String ACCESS_TOKEN_REISSUE_URL = "/v1/access";
        String refreshToken = jwtUtil.createRefreshToken(memberEntity);

        //when
        ResultActions actions = mockMvc.perform(post(ACCESS_TOKEN_REISSUE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(jwtUtil.createCookie("refreshToken", refreshToken)));

        //then
        actions.andExpect(status().isOk());
        actions.andDo(print());
        actions.andExpect(jsonPath("$.accessToken").isNotEmpty());
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