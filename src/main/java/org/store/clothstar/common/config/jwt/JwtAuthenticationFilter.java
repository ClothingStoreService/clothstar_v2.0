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
import org.store.clothstar.common.error.ErrorCode;
import org.store.clothstar.common.error.exception.NotFoundMemberException;
import org.store.clothstar.member.domain.CustomUserDetails;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.repository.MemberRepository;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

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

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER));

        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
