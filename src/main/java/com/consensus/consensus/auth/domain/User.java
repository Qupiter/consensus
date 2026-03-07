package com.consensus.consensus.auth.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private boolean emailVerified;

    private String passwordHash;

    private LocalDateTime createdAt;

    protected User() {}

    public User(String email, String passwordHash) {
        this.email = email;
        this.emailVerified = false;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
    }

    public String getEmail() {
        return email;
    }

    public boolean passwordMatches(String rawPassword, PasswordHasher hasher) {
        return hasher.verify(rawPassword, passwordHash);
    }

    public void markEmailVerified() {
        this.emailVerified = true;
    }
}