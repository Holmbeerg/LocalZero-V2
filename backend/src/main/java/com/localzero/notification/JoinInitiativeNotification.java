package com.localzero.notification;

import com.localzero.model.Initiative;
import com.localzero.model.Notification;
import com.localzero.model.User;
import com.localzero.model.enums.NotificationType;

import java.util.Map;

public class JoinInitiativeNotification extends BaseNotification {

    public JoinInitiativeNotification(Map<String, Object> data) {
        super(data);
    }

    @Override
    protected void validateData() {
        getRequiredData("initiative");
        getRequiredData("joinedBy");
    }

    @Override
    public Notification create() {
        Initiative initiative = getRequiredData("initiative");
        User joinedBy = getRequiredData("joinedBy");
        Notification notification = new Notification();

        notification.setType(NotificationType.JOIN_INITIATIVE);
        notification.setTitle("New Member Joined Your Initiative");
        notification.setMessage(joinedBy.getName() + " has joined your initiative: " + initiative.getTitle());
        notification.setCreatedBy(joinedBy);

        return notification;
    }
}