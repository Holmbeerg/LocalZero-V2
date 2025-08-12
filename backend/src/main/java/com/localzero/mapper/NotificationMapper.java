package com.localzero.mapper;

import com.localzero.dto.NotificationResponse;
import com.localzero.dto.NotificationSummaryResponse;
import com.localzero.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationResponse toResponse(Notification notification) {
        if (notification == null) {
            return null;
        }

        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public NotificationSummaryResponse toSummaryResponse(Notification notification) {
        if (notification == null) {
            return null;
        }

        return NotificationSummaryResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}