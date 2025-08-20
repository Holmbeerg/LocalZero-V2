package com.localzero.notification;

import com.localzero.model.Notification;
import com.localzero.model.enums.NotificationType;

import java.util.Map;

public class MessageNotification extends BaseNotification {
    public MessageNotification(Map<String, Object> data) {
        super(data);
    }

    @Override
    protected void validateData() {
        getRequiredData("message");
        getRequiredData("senderName");
    }

    @Override
    public Notification create() {
        String message = getRequiredData("message").toString();
        String senderName = getRequiredData("senderName").toString();

        Notification notification = new Notification();
        notification.setType(NotificationType.NEW_MESSAGE);
        notification.setTitle("New Message from " + senderName);

        String truncatedMessage = message.length() > 100
                ? message.substring(0, 100) + "..."
                : message;
        notification.setMessage(truncatedMessage);

        return notification;
    }
}