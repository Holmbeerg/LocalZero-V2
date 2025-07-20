package com.localzero.dto;

import com.localzero.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private Long referenceId;
    private String referenceType;
    private UserSummaryResponse recipient;
}