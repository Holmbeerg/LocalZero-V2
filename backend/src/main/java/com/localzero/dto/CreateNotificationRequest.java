package com.localzero.dto;

import com.localzero.model.enums.NotificationType;
import lombok.Data;

@Data
public class CreateNotificationRequest {
    private Long recipientId;
    private String title;
    private String message;
    private NotificationType type;
    private Long referenceId;
    private String referenceType;
}