package com.localzero.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record LogEcoActionRequest(
        @NotNull long actionId,

        @JsonFormat(pattern = "yyyy-MM-dd") // maybe not needed?
        @NotNull LocalDate date
) {}
