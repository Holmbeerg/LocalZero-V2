package com.localzero.model.dto;

import com.localzero.model.enums.InitiativeCategory;
import com.localzero.model.enums.Neighborhood;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateInitiativeRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull Neighborhood location,
        @NotNull InitiativeCategory category,
        boolean isPublic,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) { }
