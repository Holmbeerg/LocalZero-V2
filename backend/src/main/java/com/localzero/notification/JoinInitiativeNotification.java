package com.localzero.notification;

import com.localzero.model.Notification;
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
        String initiativeName = getRequiredData("initiative").toString();
        String joinedByName = getRequiredData("joinedBy").toString();

        Notification notification = new Notification();
        notification.setType(NotificationType.JOIN_INITIATIVE);
        notification.setTitle("New Member Joined");
        notification.setMessage(joinedByName + " has joined your initiative: " + initiativeName);
        return notification;
    }
}