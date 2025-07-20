package com.localzero.notification;

import com.localzero.model.enums.NotificationType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationFactory {
    public BaseNotification createNotification(NotificationType type, Map<String, Object> data) {
        return switch (type) {
            case NEW_INITIATIVE -> new NewInitiativeNotification(data);
            case POST_COMMENT -> new PostCommentNotification(data);
            case NEW_LIKE_ON_POST -> new PostLikeNotification(data);
            case COMMENT_REPLY -> new CommentReplyNotification(data);
            case NEW_MESSAGE -> new MessageNotification(data);
            default -> throw new IllegalArgumentException("Unsupported notification type: " + type);
        };
    }
}