package com.localzero.dto;

import com.localzero.model.Notification;
import com.localzero.model.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationSummaryResponse(
        Long id,
        String title,
        String message,
        String type,
        LocalDateTime createdAt
) {
    public static NotificationSummaryResponse fromEntity(Notification notification) {
        return new NotificationSummaryResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType().name(),
                notification.getCreatedAt()
        );
    }
}