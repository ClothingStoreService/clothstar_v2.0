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
import org.store.clothstar.member.util.CreateObject;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class JwtUnitTest {
    @Autowired
    private JwtUtil jwtUtil;

    @DisplayName("access 토큰이 생성되는지 확인")
    @Test
    void createAccessTokenTest() {
        //given
        Member member = CreateObject.getMemberByCreateMemberRequestDTO();

        //when
        String accessToken = jwtUtil.createAccessToken(member);
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
        Member member = CreateObject.getMemberByCreateMemberRequestDTO();

        //when
        String refreshToken = jwtUtil.createRefreshToken(member);
        String tokenType = jwtUtil.getTokenType(refreshToken);

        //then
        Assertions.assertThat(refreshToken).isNotNull();
        Assertions.assertThat(tokenType).isEqualTo("REFRESH_TOKEN");
    }
}