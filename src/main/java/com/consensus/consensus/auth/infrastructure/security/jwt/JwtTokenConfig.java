package com.consensus.consensus.auth.infrastructure.security.jwt;

import javax.crypto.SecretKey;

public record JwtTokenConfig(
        SecretKey key,
        long expirationSeconds,
        String issuer,
        JwtTokenType type
) {}