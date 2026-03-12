package com.consensus.consensus.auth.application;

import com.consensus.consensus.auth.application.exceptions.InvalidCredentialsException;
import com.consensus.consensus.auth.dto.AuthResponse;
import com.consensus.consensus.auth.infrastructure.jpa.TokenJpaEntity;
import com.consensus.consensus.auth.infrastructure.jpa.UserJpaEntity;
import com.consensus.consensus.auth.infrastructure.repository.TokenRepository;
import com.consensus.consensus.auth.infrastructure.repository.UserRepository;
import com.consensus.consensus.auth.infrastructure.security.jwt.JwtService;
import com.consensus.consensus.auth.infrastructure.security.jwt.JwtTokenConfig;
import com.consensus.consensus.auth.infrastructure.security.jwt.JwtTokenType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final JwtTokenConfig accessTokenConfig;
    private final JwtTokenConfig refreshTokenConfig;

    public RefreshTokenService(
            TokenRepository tokenRepository,
            UserRepository userRepository,
            JwtService jwtService,
            @Qualifier("accessTokenConfig") JwtTokenConfig accessTokenConfig,
            @Qualifier("refreshTokenConfig") JwtTokenConfig refreshTokenConfig
    ) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.accessTokenConfig = accessTokenConfig;
        this.refreshTokenConfig = refreshTokenConfig;
    }

    @Transactional
    public AuthResponse refresh(String rawRefreshToken) {

        TokenJpaEntity storedToken = tokenRepository
                .findByTokenAndTypeAndUsedFalseAndRevokedFalse(rawRefreshToken, JwtTokenType.REFRESH)
                .orElseThrow(InvalidCredentialsException::new);

        // Validate the JWT signature and expiry
        UUID userId = jwtService.extractUserId(rawRefreshToken, refreshTokenConfig.key());

        UserJpaEntity userJpa = userRepository.findById(userId)
                .orElseThrow(InvalidCredentialsException::new);

        // Rotate: mark old refresh token as used
        storedToken.markUsed();
        tokenRepository.save(storedToken);

        // Issue new tokens
        String newAccessToken = jwtService.generateToken(userJpa.getId(), accessTokenConfig);
        String newRefreshToken = jwtService.generateToken(userJpa.getId(), refreshTokenConfig);

        tokenRepository.save(TokenJpaEntity.create(
                userJpa, newAccessToken, accessTokenConfig.type(), expiresAt(accessTokenConfig.expirationSeconds())));

        tokenRepository.save(TokenJpaEntity.create(
                userJpa, newRefreshToken, refreshTokenConfig.type(), expiresAt(refreshTokenConfig.expirationSeconds())));

        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    private LocalDateTime expiresAt(long seconds) {
        return LocalDateTime.ofInstant(Instant.now().plusSeconds(seconds), ZoneOffset.UTC);
    }
}
