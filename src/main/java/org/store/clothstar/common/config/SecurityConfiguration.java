package org.store.clothstar.common.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.store.clothstar.common.config.jwt.JwtAuthenticationFilter;
import org.store.clothstar.common.config.jwt.JwtUtil;
import org.store.clothstar.common.config.jwt.LoginFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().disable()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable();

        http.authorizeRequests()
                .antMatchers("/", "/login", "/v1/login", "/signup").permitAll()
                .antMatchers("/user**").authenticated()
                .antMatchers("/admin**").hasRole("ADMIN")
                .antMatchers("/seller**").hasRole("SELLER")
                .anyRequest().permitAll();

        // JWT 토큰 인증 방식 사용하기에 session 유지 비활성화
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //UsernamePasswordAuthenticationFilter 대신에 LoginFilter가 실행된다.
        //LoginFilter 이전에 jwtAhthenticationFilter가 실행된다.
        http.addFilterBefore(jwtAuthenticationFilter, LoginFilter.class);
        http.addFilterAt(new LoginFilter(authenticationManager(), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}