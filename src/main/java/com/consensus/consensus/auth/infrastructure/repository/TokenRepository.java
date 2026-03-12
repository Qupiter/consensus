package com.consensus.consensus.auth.infrastructure.repository;

import com.consensus.consensus.auth.infrastructure.jpa.TokenJpaEntity;
import com.consensus.consensus.auth.infrastructure.jpa.UserJpaEntity;
import com.consensus.consensus.auth.infrastructure.security.jwt.JwtTokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<TokenJpaEntity, UUID> {

    Optional<TokenJpaEntity> findByToken(String token);

    Optional<TokenJpaEntity> findByTokenAndType(String token, JwtTokenType type);

    Optional<TokenJpaEntity> findByTokenAndTypeAndUsedFalseAndRevokedFalse(
            String token,
            JwtTokenType type
    );

    void deleteByExpiresAtBefore(LocalDateTime time);

    boolean existsByUserAndTypeAndUsedFalseAndRevokedFalse(UserJpaEntity user, JwtTokenType type);
}
