package org.store.clothstar.common.config.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.store.clothstar.member.domain.Member;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
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

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .claim("tokenType", tokenType)
                .claim("email", memberEmail)
                .claim("id", memberId)
                .claim("role", member.getRole())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
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
                    .parseClaimsJws(token);
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