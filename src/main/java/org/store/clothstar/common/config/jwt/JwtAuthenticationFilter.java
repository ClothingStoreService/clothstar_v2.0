package org.store.clothstar.common.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.store.clothstar.member.domain.Account;
import org.store.clothstar.member.domain.CustomUserDetails;
import org.store.clothstar.member.repository.AccountRepository;
import org.store.clothstar.member.repository.AuthorizationRepository;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final AccountRepository accountRepository;
    private final AuthorizationRepository authorizationRepository;

    /**
     * 요청이 왔을때 token이 있는지 확인하고 token에 대한 유효성 검사를 진행한다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtUtil.resolveToken((HttpServletRequest) request);
        log.info("doFilterInternal() 실행, token={}", token);

        if (token == null) {
            log.info("JWT 토큰정보가 없습니다.");
        } else if (!jwtUtil.validateToken(token)) {
            log.info("JWT 토큰이 만료되거나 잘못되었습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            authenticateUserWithToken(token);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUserWithToken(String token) {
        Long memberId = jwtUtil.getMemberId(token);
        log.info("refresh 토큰 memberId: {}", memberId);

        Account account = accountRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("계정정보를 찾을수 없습니다."));

        log.error("account toString : {}", account.toString());

        CustomUserDetails customUserDetails = new CustomUserDetails(account);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
