package com.localzero.mapper;

import com.localzero.dto.NotificationResponse;
import com.localzero.dto.NotificationSummaryResponse;
import com.localzero.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationMapper {
    private final UserMapper userMapper;

    public NotificationResponse toResponse(Notification notification) {
        if (notification == null) {
            return null;
        }

        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.getCreatedAt(),
                notification.getCreatedBy() != null ? 
                    userMapper.toUserSummaryResponse(notification.getCreatedBy()) : 
                    null
        );
    }

    public NotificationSummaryResponse toSummaryResponse(Notification notification) {
        if (notification == null) {
            return null;
        }
        return new NotificationSummaryResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType().name(),
                notification.getCreatedAt()
        );
    }
}