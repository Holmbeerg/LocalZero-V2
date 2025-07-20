package com.localzero.notification;

import com.localzero.model.Notification;
import com.localzero.model.User;
import com.localzero.model.enums.NotificationType;

import java.util.Map;

public abstract class BaseNotification {
    protected final Map<String, Object> data;
    protected final Notification notification;

    protected BaseNotification(Map<String, Object> data) {
        this.data = data;
        this.notification = new Notification();
        this.notification.setCreatedBy((User) data.get("actor"));
    }

    public final Notification create() {
        validateData();
        prepareContent();
        setType();
        return notification;
    }

    protected abstract void validateData();
    protected abstract void prepareContent();
    protected abstract void setType();

    protected <T> T getRequiredData(String key, Class<T> type) {
        if (!data.containsKey(key)) {
            throw new IllegalArgumentException("Missing required data: " + key);
        }
        return type.cast(data.get(key));
    }
}