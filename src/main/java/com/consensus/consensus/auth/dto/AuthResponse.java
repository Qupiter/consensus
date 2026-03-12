package com.consensus.consensus.auth.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {}
