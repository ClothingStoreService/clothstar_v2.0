package org.store.clothstar.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private final String BEARER_TOKEN = "Bearer Token";
    private final String BEARER = "Bearer";
    private final String AUTHORIZATION = "Authorization";
    private final String JWT = "JWT";

    @Bean
    public GroupedOpenApi groupedAllOpenApi() {
        return GroupedOpenApi.builder()
                .group("All")
                .pathsToMatch("/v1/**", "/v2/**", "/v3/**")
                .build();
    }

    @Bean
    public GroupedOpenApi groupedMemberOpenApi() {
        return GroupedOpenApi.builder()
                .group("Member")
                .pathsToMatch("/v1/members/**", "/v1/sellers/**")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        Info info = new Info().title("clothstar-v2 Project API")
                .description("의류 쇼핑몰 2차 프로젝트 입니다.")
                .version("v0.2");

        SecurityScheme apiKey = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name(AUTHORIZATION)
                .scheme(BEARER)
                .bearerFormat(JWT);

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(BEARER_TOKEN);

        return new OpenAPI().info(info)
                .components(new Components().addSecuritySchemes(BEARER_TOKEN, apiKey))
                .addSecurityItem(securityRequirement);
    }
}