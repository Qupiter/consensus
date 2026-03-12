package com.consensus.consensus.auth.application;

import com.consensus.consensus.auth.application.exceptions.EmailAlreadyExistsException;
import com.consensus.consensus.auth.domain.User;
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
public class RegisterUserService {

    private final JwtService jwtService;
    private final JwtTokenConfig emailTokenConfig;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    public RegisterUserService(
            JwtService jwtService,
            @Qualifier("emailTokenConfig") JwtTokenConfig emailTokenConfig,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            TokenRepository tokenRepository
    ) {
        this.jwtService = jwtService;
        this.emailTokenConfig = emailTokenConfig;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void register(String email, String password) {

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        String hashed = passwordEncoder.encode(password);

        User user = new User(email, hashed);

        UserJpaEntity userJpa = userRepository.save(user.toJpa());

        String emailJwt = jwtService.generateToken(userJpa.getId(), emailTokenConfig);

        LocalDateTime expiresAt = LocalDateTime.ofInstant(
                Instant.now().plusSeconds(emailTokenConfig.expirationSeconds()),
                ZoneOffset.UTC
        );

        TokenJpaEntity token = TokenJpaEntity.create(
                userJpa,
                emailJwt,
                emailTokenConfig.type(),
                expiresAt
        );

        tokenRepository.save(token);

        // TODO: dispatch email verification event / send email
    }
}
