package com.localzero.notification;

import com.localzero.model.Initiative;
import com.localzero.model.enums.NotificationType;

import java.util.Map;

public class NewInitiativeNotification extends BaseNotification {

    public NewInitiativeNotification(Map<String, Object> data) {
        super(data);
    }

    @Override
    protected void validateData() {
        getRequiredData("initiative", Initiative.class);
    }

    @Override
    protected void prepareContent() {
        Initiative initiative = getRequiredData("initiative", Initiative.class);
        notification.setTitle("New Initiative in Your Area");
        notification.setMessage(String.format(
                "A new initiative '%s' has been created in your area!",
                initiative.getTitle()
        ));
    }

    @Override
    protected void setType() {
        notification.setType(NotificationType.NEW_INITIATIVE);
    }
}