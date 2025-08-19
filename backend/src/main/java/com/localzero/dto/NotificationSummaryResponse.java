package com.localzero.dto;

import java.time.LocalDateTime;

public record NotificationSummaryResponse(
        Long id,
        String title,
        String message,
        String type,
        LocalDateTime createdAt
) { }