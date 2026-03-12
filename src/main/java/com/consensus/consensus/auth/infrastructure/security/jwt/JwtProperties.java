package com.consensus.consensus.auth.infrastructure.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String issuer;

    private String accessSecret;
    private String refreshSecret;
    private String emailSecret;
    private String passwordSecret;

    private long accessExpirationSeconds;
    private long refreshExpirationSeconds;
    private long emailExpirationSeconds;
    private long passwordExpirationSeconds;
}
