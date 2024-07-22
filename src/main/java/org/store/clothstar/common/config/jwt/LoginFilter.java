package org.store.clothstar.common.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.common.util.MessageDTOBuilder;
import org.store.clothstar.member.domain.CustomUserDetails;
import org.store.clothstar.member.domain.Member;
import org.store.clothstar.member.dto.request.MemberLoginRequest;

import java.io.IOException;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/v1/members/login");
    }

    /**
     * 로그인 창에서 입력한 id, password를 받아서
     * Authentication Manager에 던져 줘야 하는데 그 DTO역할을 하는 객체가 UsernamePasswordAuthenticationToken이다.
     * Authentication Manager에 전달하면 최종적으로 Authentication에 전달 된다.
     * return 하면 Authentication Manager에 던져진다.
     * <p>
     * AuthenticationManager에 던지기 위해서 주입을 받아야 한다.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.info("로그인 진행");

        ObjectMapper om = new ObjectMapper();
        MemberLoginRequest memberLoginRequest;
        String email;
        String password;

        try {
            memberLoginRequest = om.readValue(request.getInputStream(), MemberLoginRequest.class);
            log.info("login parameter memberLoginRequest: {}", memberLoginRequest.toString());

            email = memberLoginRequest.getEmail();
            password = memberLoginRequest.getPassword();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UsernamePasswordAuthenticationToken authTokenDTO
                = new UsernamePasswordAuthenticationToken(email, password, null);

        return authenticationManager.authenticate(authTokenDTO);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        log.info("로그인 성공");
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Member member = customUserDetails.getMember();
        log.info("member: {}", member.toString());

        String accessToken = jwtUtil.createAccessToken(member);
        log.info("생성 accessToken: Bearer {}", accessToken);
        String refreshToken = jwtUtil.createRefreshToken(member);
        log.info("생성 refreshToken: Bearer {}", refreshToken);

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(jwtUtil.createCookie("refreshToken", refreshToken));
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(HttpServletResponse.SC_OK, "로그인 성공 하였습니다.");
        ObjectMapper om = new ObjectMapper();

        response.getWriter().print(om.writeValueAsString(messageDTO));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(HttpServletResponse.SC_UNAUTHORIZED, errorMessage(failed));
        ObjectMapper om = new ObjectMapper();

        response.getWriter().print(om.writeValueAsString(messageDTO));
    }

    private String errorMessage(AuthenticationException failed) {
        String errorMessage = null;

        if (failed instanceof BadCredentialsException) {
            errorMessage = "이메일 또는 비밀번호가 올바르지 않습니다. 다시 확인해주세요.";
        } else if (failed instanceof DisabledException) {
            errorMessage = "계정이 비활성화 되어있습니다. 이메일 인증을 완료해주세요";
        }

        return errorMessage;
    }
}
