package com.consensus.consensus.auth.infrastructure.security.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtKeyProvider {

    private final JwtProperties props;

    public JwtKeyProvider(JwtProperties props) {
        this.props = props;
    }

    public String issuer() {
        return props.getIssuer();
    }

    public SecretKey accessKey() {
        return Keys.hmacShaKeyFor(props.getAccessSecret().getBytes());
    }

    public SecretKey refreshKey() {
        return Keys.hmacShaKeyFor(props.getRefreshSecret().getBytes());
    }

    public SecretKey emailKey() {
        return Keys.hmacShaKeyFor(props.getEmailSecret().getBytes());
    }

    public SecretKey passwordKey() {
        return Keys.hmacShaKeyFor(props.getPasswordSecret().getBytes());
    }
}