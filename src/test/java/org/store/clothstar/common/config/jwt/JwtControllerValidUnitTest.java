package org.store.clothstar.common.config.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.store.clothstar.common.error.ErrorCode;
import org.store.clothstar.common.error.exception.RefreshTokenInValidException;
import org.store.clothstar.common.error.exception.RefreshTokenNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class JwtControllerValidUnitTest {
    @InjectMocks
    private JwtController jwtController;

    @Mock
    private JwtService jwtService;

    @Mock
    private JwtUtil jwtUtil;

    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);

    @DisplayName("access 토큰을 재발급 할때 refresh 토큰이 없으면 에러 메시지를 반환한다.")
    @WithMockUser
    @Test
    void accessToken_reissue_refreshToken_notFound_validCheck() throws Exception {
        //given
        given(jwtService.getRefreshToken(request)).willReturn(null);

        //when
        Throwable exception = assertThrows(RefreshTokenNotFoundException.class, () -> {
            jwtController.reissue(request, response);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.NOT_FOUND_REFRESH_TOKEN.getMessage());
    }

    @DisplayName("refresh 유효하지 않으면 에러메시지를 반환한다.")
    @Test
    void accessToken_reissue_refreshToken_inValid_Check() {
        //given
        given(jwtService.getRefreshToken(request)).willReturn(anyString());
        given(jwtUtil.validateToken("anyString")).willReturn(false);

        //when
        Throwable exception = assertThrows(RefreshTokenInValidException.class, () -> {
            jwtController.reissue(request, response);
        });

        //then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN.getMessage());
    }
}