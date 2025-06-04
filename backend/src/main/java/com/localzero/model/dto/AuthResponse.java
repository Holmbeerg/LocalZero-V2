package com.localzero.model.dto;

public record AuthResponse(
    String token,
    UserResponse userResponse
) {}
