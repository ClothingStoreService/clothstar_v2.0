package org.store.clothstar.common.config.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.store.clothstar.member.domain.Member;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    private final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        secretKey = new SecretKeySpec(jwtProperties.getSecretKey().getBytes(),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    //http 헤더 Authorization의 값이 jwt token인지 확인하고 token값을 넘기는 메서드
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 30); //refresh token하고 같은 생명주기 30분으로 세팅
        cookie.setHttpOnly(true); //자바스크립트로 쿠키 접근 못하게 막음

        return cookie;
    }

    public String createAccessToken(Member member) {
        return createToken(member, jwtProperties.getAccessTokenValidTimeMillis(), ACCESS_TOKEN);
    }

    public String createRefreshToken(Member member) {
        return createToken(member, jwtProperties.getRefreshTokenValidTimeMillis(), REFRESH_TOKEN);
    }

    private String createToken(Member member, Long tokenValidTimeMillis, String tokenType) {
        Long memberId = member.getMemberId();
        String memberEmail = member.getEmail();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + tokenValidTimeMillis);

        Header jwtHeader = Jwts.header()
                .type("JWT")
                .build();

        return Jwts.builder()
                .header().add(jwtHeader).and()
                .issuedAt(currentDate)
                .expiration(expireDate)
                .claim("tokenType", tokenType)
                .claim("email", memberEmail)
                .claim("id", memberId)
                .claim("role", member.getRole())
                .signWith(secretKey)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getMemberId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    public String getTokenType(String token) {
        Claims claims = getClaims(token);
        return claims.get("tokenType", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}