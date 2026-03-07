package com.consensus.consensus.auth.application;

import com.consensus.consensus.auth.domain.PasswordHasher;
import com.consensus.consensus.auth.domain.User;
import com.consensus.consensus.auth.infrastructure.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordHasher hasher;

    public AuthService(UserRepository userRepository, PasswordHasher hasher) {
        this.userRepository = userRepository;
        this.hasher = hasher;
    }

    public User register(String email, String password) {

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User(email, hasher.hash(password));
        return userRepository.save(user);
    }

    public User login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.passwordMatches(password, hasher)) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }
}