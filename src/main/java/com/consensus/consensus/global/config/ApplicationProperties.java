package com.consensus.consensus.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {

    private String jwtSecret;
    private long jwtExpirationMs;

}