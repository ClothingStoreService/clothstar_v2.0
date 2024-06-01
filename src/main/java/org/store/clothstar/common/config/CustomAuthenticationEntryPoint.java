package org.store.clothstar.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.store.clothstar.common.dto.MessageDTO;
import org.store.clothstar.common.util.MessageDTOBuilder;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("인증 실패 로직 실행");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        MessageDTO messageDTO = MessageDTOBuilder.buildMessage(HttpServletResponse.SC_UNAUTHORIZED, "권한이 없습니다.");
        ObjectMapper om = new ObjectMapper();
        response.getWriter().write(om.writeValueAsString(messageDTO));
    }
}
