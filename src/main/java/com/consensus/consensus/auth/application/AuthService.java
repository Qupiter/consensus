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

    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already taken");
        }
        User user = new User(username, hasher.hash(password));
        return userRepository.save(user);
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.passwordMatches(password, hasher)) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }
}