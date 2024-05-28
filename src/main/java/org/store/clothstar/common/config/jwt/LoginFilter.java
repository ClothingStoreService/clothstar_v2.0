package org.store.clothstar.common.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
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
        setFilterProcessesUrl("/v1/login");
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

        log.info("attemptAuthentication() 실행");
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
        response.addCookie(createCookie("refreshToken", refreshToken));
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(HttpServletResponse.SC_OK, "로그인 성공 하였습니다.", true);
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

        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(HttpServletResponse.SC_UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다. 다시 확인해주세요.", false);
        ObjectMapper om = new ObjectMapper();

        response.getWriter().print(om.writeValueAsString(messageDTO));
    }

    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 30); //refresh token하고 같은 생명주기 30분으로 세팅
        cookie.setHttpOnly(true); //자바스크립트로 쿠키 접근 못하게 막음

        return cookie;
    }
}
