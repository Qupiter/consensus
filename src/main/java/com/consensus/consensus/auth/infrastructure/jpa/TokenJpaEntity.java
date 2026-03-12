package com.consensus.consensus.auth.infrastructure.jpa;

import com.consensus.consensus.auth.infrastructure.security.jwt.JwtTokenType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "auth_tokens")
public class TokenJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String token;

    @Enumerated(EnumType.STRING)
    private JwtTokenType type;

    private boolean used;

    private boolean revoked;

    private LocalDateTime expiresAt;

    @Getter
    @ManyToOne(optional = false)
    private UserJpaEntity user;

    protected TokenJpaEntity() {}

    private TokenJpaEntity(UserJpaEntity user, String token, JwtTokenType type, LocalDateTime expiresAt) {
        this.user = user;
        this.token = token;
        this.type = type;
        this.expiresAt = expiresAt;
        this.used = false;
        this.revoked = false;
    }

    public static TokenJpaEntity create(UserJpaEntity user, String token, JwtTokenType type, LocalDateTime expiresAt) {
        return new TokenJpaEntity(user, token, type, expiresAt);
    }

    public void markUsed() {
        this.used = true;
    }

    public void revoke() {
        this.revoked = true;
    }
}
