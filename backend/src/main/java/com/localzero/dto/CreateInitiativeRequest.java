package com.localzero.dto;

import com.localzero.model.enums.InitiativeCategory;
import com.localzero.model.enums.Neighborhood;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateInitiativeRequest(
        @NotBlank String title,
        @NotBlank String description,
        // @NotNull Neighborhood location, we don't use this for now, users can only create initiatives in their own neighborhood
        @NotNull InitiativeCategory category,
        boolean isPublic,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) { }
