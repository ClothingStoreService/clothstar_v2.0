package org.store.clothstar.common.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.store.clothstar.common.config.jwt.JwtAuthenticationFilter;
import org.store.clothstar.common.config.jwt.JwtUtil;
import org.store.clothstar.common.config.jwt.LoginFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
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
        http.cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable());

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/", "/login", "/userPage", "/sellerPage", "/adminPage"
                        , "/v1/login", "/signup", "/v1/members/email/**", "/v1/access",
                        "/v1/categories/**", "/v1/products/**", "/v1/productLines/**", "/v2/productLines/**",
                        "/v1/orderdetails",
                        "/v1/seller/orders/**", "/v1/seller/orders", "/v1/orders/**", "/v1/orders").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/members").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/sellers/**").authenticated()
                .requestMatchers("/seller/**", "/v1/sellers/**").hasRole("SELLER")
                .requestMatchers("/admin/**", "/v1/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/v1/members").hasRole("ADMIN")
                .anyRequest().authenticated()
        );
//
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint));

        //JWT 토큰 인증 방식 사용하기에 session 유지 비활성화
        http.sessionManagement(sessionManagement
                -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //UsernamePasswordAuthenticationFilter 대신에 LoginFilter가 실행된다.
        //LoginFilter 이전에 jwtAhthenticationFilter가 실행된다.
        http.addFilterBefore(jwtAuthenticationFilter, LoginFilter.class);
        http.addFilterAt(new LoginFilter(authenticationManager(), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}