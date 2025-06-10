package com.localzero.model.dto;

import com.localzero.model.Role;
import com.localzero.model.enums.Neighborhood;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponse(
        Long id,
        String email,
        String name,
        Neighborhood location,
        LocalDateTime createdAt,
        List<Role> roles
) {}