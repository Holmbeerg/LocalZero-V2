package com.localzero.model.dto;
import com.localzero.model.enums.Neighborhood;
import jakarta.validation.constraints.Email; // TODO: should these be used
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
    @NotNull @Email String email,
    @NotBlank String password, // TODO: @Size(min = 6)? must implement in database too
    @NotBlank String name,
    @NotNull Neighborhood location
) {}

