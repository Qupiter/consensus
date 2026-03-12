package com.consensus.consensus.auth.application;

import com.consensus.consensus.auth.application.exceptions.InvalidCredentialsException;
import com.consensus.consensus.auth.domain.User;
import com.consensus.consensus.auth.dto.AuthResponse;
import com.consensus.consensus.auth.infrastructure.jpa.TokenJpaEntity;
import com.consensus.consensus.auth.infrastructure.jpa.UserJpaEntity;
import com.consensus.consensus.auth.infrastructure.repository.TokenRepository;
import com.consensus.consensus.auth.infrastructure.repository.UserRepository;
import com.consensus.consensus.auth.infrastructure.security.jwt.JwtService;
import com.consensus.consensus.auth.infrastructure.security.jwt.JwtTokenConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class LoginUserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtTokenConfig accessTokenConfig;
    private final JwtTokenConfig refreshTokenConfig;

    public LoginUserService(
            UserRepository userRepository,
            TokenRepository tokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            @Qualifier("accessTokenConfig") JwtTokenConfig accessTokenConfig,
            @Qualifier("refreshTokenConfig") JwtTokenConfig refreshTokenConfig
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.accessTokenConfig = accessTokenConfig;
        this.refreshTokenConfig = refreshTokenConfig;
    }

    @Transactional
    public AuthResponse login(String email, String password) {

        UserJpaEntity userJpa = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        User user = userJpa.toDomain();

        if (!user.passwordMatches(password, passwordEncoder)) {
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtService.generateToken(userJpa.getId(), accessTokenConfig);
        String refreshToken = jwtService.generateToken(userJpa.getId(), refreshTokenConfig);

        LocalDateTime accessExpiresAt = expiresAt(accessTokenConfig.expirationSeconds());
        LocalDateTime refreshExpiresAt = expiresAt(refreshTokenConfig.expirationSeconds());

        tokenRepository.save(TokenJpaEntity.create(
                userJpa, accessToken, accessTokenConfig.type(), accessExpiresAt));

        tokenRepository.save(TokenJpaEntity.create(
                userJpa, refreshToken, refreshTokenConfig.type(), refreshExpiresAt));

        return new AuthResponse(accessToken, refreshToken);
    }

    private LocalDateTime expiresAt(long seconds) {
        return LocalDateTime.ofInstant(Instant.now().plusSeconds(seconds), ZoneOffset.UTC);
    }
}
