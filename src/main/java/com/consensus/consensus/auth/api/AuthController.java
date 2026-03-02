package com.consensus.consensus.auth.api;

import com.consensus.consensus.auth.application.AuthService;
import com.consensus.consensus.auth.domain.User;
import com.consensus.consensus.auth.dto.AuthRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid AuthRequest req) {
        return ResponseEntity.ok(service.register(req.username(), req.password()));
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody @Valid AuthRequest req) {
        return ResponseEntity.ok(service.login(req.username(), req.password()));
    }
}