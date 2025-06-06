package com.localzero.model.dto;

import com.localzero.model.Role;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponse(
        Long id,
        String email,
        String name,
        String location,
        LocalDateTime createdAt,
        List<Role> roles
) {}