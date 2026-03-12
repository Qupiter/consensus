package com.consensus.consensus.auth.infrastructure.security.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wires a named {@link JwtTokenConfig} bean for each token type so they can be
 * injected by qualifier in services.
 */
@Configuration
public class JwtBeanConfig {

    private final JwtKeyProvider keys;
    private final JwtProperties props;

    public JwtBeanConfig(JwtKeyProvider keys, JwtProperties props) {
        this.keys = keys;
        this.props = props;
    }

    @Bean("accessTokenConfig")
    public JwtTokenConfig accessTokenConfig() {
        return new JwtTokenConfig(
                keys.accessKey(),
                props.getAccessExpirationSeconds(),
                props.getIssuer(),
                JwtTokenType.ACCESS
        );
    }

    @Bean("refreshTokenConfig")
    public JwtTokenConfig refreshTokenConfig() {
        return new JwtTokenConfig(
                keys.refreshKey(),
                props.getRefreshExpirationSeconds(),
                props.getIssuer(),
                JwtTokenType.REFRESH
        );
    }

    @Bean("emailTokenConfig")
    public JwtTokenConfig emailTokenConfig() {
        return new JwtTokenConfig(
                keys.emailKey(),
                props.getEmailExpirationSeconds(),
                props.getIssuer(),
                JwtTokenType.EMAIL_VERIFICATION
        );
    }

    @Bean("passwordTokenConfig")
    public JwtTokenConfig passwordTokenConfig() {
        return new JwtTokenConfig(
                keys.passwordKey(),
                props.getPasswordExpirationSeconds(),
                props.getIssuer(),
                JwtTokenType.PASSWORD_RESET
        );
    }
}
