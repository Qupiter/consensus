package com.consensus.consensus.auth.api;

import com.consensus.consensus.auth.application.LoginUserService;
import com.consensus.consensus.auth.application.RefreshTokenService;
import com.consensus.consensus.auth.application.RegisterUserService;
import com.consensus.consensus.auth.dto.AuthResponse;
import com.consensus.consensus.auth.dto.LoginRequest;
import com.consensus.consensus.auth.dto.RefreshRequest;
import com.consensus.consensus.auth.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserService registerUserService;
    private final LoginUserService loginUserService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(
            RegisterUserService registerUserService,
            LoginUserService loginUserService,
            RefreshTokenService refreshTokenService
    ) {
        this.registerUserService = registerUserService;
        this.loginUserService = loginUserService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid RegisterRequest request) {
        registerUserService.register(request.email(), request.password());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        return loginUserService.login(request.email(), request.password());
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody @Valid RefreshRequest request) {
        return refreshTokenService.refresh(request.refreshToken());
    }
}
