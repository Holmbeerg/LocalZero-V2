package com.localzero.notification;

import com.localzero.model.Initiative;
import com.localzero.model.Notification;
import com.localzero.model.User;
import com.localzero.model.enums.NotificationType;

import java.util.Map;

public class NewInitiativeNotification extends BaseNotification {
    public NewInitiativeNotification(Map<String, Object> data) {
        super(data);
    }

    @Override
    protected void validateData() {
        getRequiredData("initiative");
        getRequiredData("createdBy");
    }

    @Override
    public Notification create() {
        Initiative initiative = getRequiredData("initiative");
        User createdBy = getRequiredData("createdBy");

        Notification notification = new Notification();
        notification.setType(NotificationType.NEW_INITIATIVE);
        notification.setTitle("New Initiative: " + initiative.getTitle());
        notification.setMessage(createdBy.getName() + " created a new initiative: " + initiative.getTitle());
        notification.setCreatedBy(createdBy);

        return notification;
    }
}