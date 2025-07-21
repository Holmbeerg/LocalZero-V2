package com.localzero.notification;

import com.localzero.model.Notification;
import com.localzero.model.User;
import com.localzero.model.enums.NotificationType;

import java.util.Map;

public class MessageNotification extends BaseNotification {

    public MessageNotification(Map<String, Object> data) {
        super(data);
    }

    @Override
    protected void validateData() {
        getRequiredData("message");
        getRequiredData("sender");
    }

    @Override
    public Notification create() {
        String message = getRequiredData("message");
        User sender = getRequiredData("sender");

        Notification notification = new Notification();
        notification.setType(NotificationType.NEW_MESSAGE);
        notification.setTitle("New Message from " + sender.getName());
        notification.setMessage(message);
        notification.setCreatedBy(sender);

        return notification;
    }
}