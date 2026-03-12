package com.consensus.consensus.auth.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtService {

    private final JwtKeyProvider keys;

    public JwtService(JwtKeyProvider keys) {
        this.keys = keys;
    }

    public String generateToken(UUID userId, JwtTokenConfig config) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId.toString())
                .claim("type", config.type())
                .issuer(config.issuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(config.expirationSeconds())))
                .signWith(config.key())
                .compact();
    }

    public UUID extractUserId(String token, SecretKey key) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return UUID.fromString(claims.getSubject());
    }
}
