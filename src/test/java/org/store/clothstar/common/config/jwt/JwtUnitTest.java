package org.store.clothstar.common.config.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.domain.MemberGrade;
import org.store.clothstar.member.domain.MemberRole;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@Transactional
class JwtUnitTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtService jwtService;

    @DisplayName("access 토큰이 생성되는지 확인")
    @Test
    void createAccessTokenTest() {
        //given
        Member member = getMember();

        //when
        String accessToken = jwtUtil.createAccessToken(getMember());
        String tokenType = jwtUtil.getTokenType(accessToken);
        System.out.println("accessToken = " + accessToken);

        //then
        Assertions.assertThat(accessToken).isNotNull();
        Assertions.assertThat(tokenType).isEqualTo("ACCESS_TOKEN");
    }

    @DisplayName("refresh 토큰이 생성되는지 확인")
    @Test
    void createRefreshTokenTest() {
        //given
        Member member = getMember();

        //when
        String refreshToken = jwtUtil.createRefreshToken(getMember());
        String tokenType = jwtUtil.getTokenType(refreshToken);

        //then
        Assertions.assertThat(refreshToken).isNotNull();
        Assertions.assertThat(tokenType).isEqualTo("REFRESH_TOKEN");
    }

    @DisplayName("refresh 토큰으로 access 토큰이 생성되는지 확인")
    @Test
    void createAccessTokenByRefreshTokenTest() {
        //given
        final String refreshToken = jwtUtil.createRefreshToken(getMember());
        String refreshTokenType = jwtUtil.getTokenType(refreshToken);

        //when
        final String accessToken = jwtService.getAccessTokenByRefreshToken(refreshToken);
        String accessTokenType = jwtUtil.getTokenType(accessToken);

        //then
        Assertions.assertThat(refreshToken).isNotNull();
        Assertions.assertThat(refreshTokenType).isEqualTo("REFRESH_TOKEN");
        Assertions.assertThat(accessTokenType).isEqualTo("ACCESS_TOKEN");
    }

    private Member getMember() {
        return Member.builder()
                .memberId(1L)
                .email("test@test.com")
                .password("test")
                .telNo("010-1234-1234")
                .role(MemberRole.USER)
                .grade(MemberGrade.BRONZE)
                .build();
    }
}