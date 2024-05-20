package org.store.clothstar.common.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {
	private String secretKey;
	private Long accessTokenValidTimeMillis;
	private Long refreshTokenValidTimeMillis;
}