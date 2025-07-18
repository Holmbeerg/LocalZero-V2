package com.localzero.dto;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MessageResponse(
        @NotNull long id,
        @NotNull UserSummaryResponse sender,
        @NotNull UserSummaryResponse receiver,
        @NotBlank String text,
        @NotNull LocalDateTime createdAt
) { }
