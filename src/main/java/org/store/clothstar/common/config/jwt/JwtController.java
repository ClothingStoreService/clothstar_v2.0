package org.store.clothstar.common.config.jwt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.store.clothstar.common.dto.AccessTokenResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "Jwt", description = "Jwt와 관련된 API 입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
public class JwtController {
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

    @Operation(summary = "access 토큰 재발급", description = "refresh 토큰으로 access 토큰을 재발급 한다.")
    @PostMapping("/v1/access")
    public ResponseEntity<AccessTokenResponse> reissue(HttpServletRequest request, HttpServletResponse response) {
        log.info("access 토큰 refresh 요청");
        String refreshToken = jwtService.getRefreshToken(request);

        if (refreshToken == null) {
            log.info("refresh 토큰이 없습니다.");
            return new ResponseEntity<>(getAccessTokenResponse(null, "refresh 토큰이 없습니다.", false),
                    HttpStatus.BAD_REQUEST);
        }

        if (!jwtUtil.validateToken(refreshToken)) {
            log.info("refresh 토큰이 만료되었거나 유효하지 않습니다.");
            return new ResponseEntity<>(
                    getAccessTokenResponse(null, "refresh 토큰이 만료되었거나 유효하지 않습니다.", false),
                    HttpStatus.BAD_REQUEST);
        }

        String accessToken = jwtService.getAccessTokenByRefreshToken(refreshToken);
        response.addHeader("Authorization", "Bearer " + accessToken);
        log.info("access 토큰이 갱신 되었습니다.");

        return ResponseEntity.ok(getAccessTokenResponse(accessToken, "access 토큰이 생성 되었습니다.", true));
    }

    private static AccessTokenResponse getAccessTokenResponse(String accessToken, String message, boolean success) {
        AccessTokenResponse accessTokenResponse = null;

        return accessTokenResponse.builder()
                .accessToken(accessToken)
                .message(message)
                .success(success)
                .build();
    }
}