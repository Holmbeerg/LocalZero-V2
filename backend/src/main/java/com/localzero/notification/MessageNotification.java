package com.localzero.notification;

import com.localzero.model.User;
import com.localzero.model.enums.NotificationType;

import java.util.Map;

public class MessageNotification extends BaseNotification {

    public MessageNotification(Map<String, Object> data) {
        super(data);
    }

    @Override
    protected void validateData() {
        getRequiredData("sender", User.class);
        getRequiredData("message", String.class);
    }

    @Override
    protected void prepareContent() {
        User sender = getRequiredData("sender", User.class);
        String message = getRequiredData("message", String.class);

        notification.setTitle("New Message from " + sender.getName());
        notification.setMessage(
                message.length() > 100
                        ? message.substring(0, 97) + "..."
                        : message
        );
    }

    @Override
    protected void setType() {
        notification.setType(NotificationType.NEW_MESSAGE);
    }
}