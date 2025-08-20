package com.localzero.notification;

import com.localzero.model.enums.NotificationType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationFactory {
    public BaseNotification createNotification(NotificationType type, Map<String, Object> data) {
        return switch (type) {
            case NEW_INITIATIVE -> new NewInitiativeNotification(data);
            case NEW_POST_IN_INITIATIVE -> new NewPostNotification(data);
            case NEW_LIKE_ON_POST -> new PostLikeNotification(data);
            case COMMENT_REPLY -> new PostReplyNotification(data);
            case NEW_MESSAGE -> new MessageNotification(data);
            case JOIN_INITIATIVE -> new JoinInitiativeNotification(data);
        };
    }
}