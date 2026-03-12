package com.consensus.consensus.auth.domain;

import com.consensus.consensus.auth.infrastructure.jpa.UserJpaEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class User {

    private UUID id;

    private String email;

    private boolean verified;

    private String passwordHash;

    private LocalDateTime createdAt;

    protected User() {}

    public User(String email, String passwordHash) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.verified = false;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
    }

    public static User reconstruct(UserJpaEntity jpa) {
        User user = new User();
        user.id = jpa.getId();
        user.email = jpa.getEmail();
        user.passwordHash = jpa.getPasswordHash();
        user.verified = jpa.isVerified();
        user.createdAt = jpa.getCreatedAt();
        return user;
    }

    public UserJpaEntity toJpa() {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setEmail(this.email);
        entity.setPasswordHash(this.passwordHash);
        entity.setVerified(this.verified);
        entity.setCreatedAt(this.createdAt);
        return entity;
    }

    public boolean passwordMatches(String rawPassword, org.springframework.security.crypto.password.PasswordEncoder encoder) {
        return encoder.matches(rawPassword, passwordHash);
    }

    public void verifyEmail() {
        this.verified = true;
    }
}
