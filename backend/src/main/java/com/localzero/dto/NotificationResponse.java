package com.localzero.dto;

import com.localzero.model.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String title,
        String message,
        NotificationType type,
        LocalDateTime createdAt,
        UserSummaryResponse createdBy
) {
    public static NotificationResponse fromEntity(com.localzero.model.Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.getCreatedAt(),
                null
        );
    }
}
