package com.localzero.notification;

import com.localzero.model.Notification;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseNotification {
    protected final Map<String, Object> data;

    protected BaseNotification(Map<String, Object> data) {
        this.data = data != null ? data : new HashMap<>();
        validateData();
    }

    public final Notification createNotification() {
        validateData();
        return buildNotification();
    }

    protected abstract void validateData();
    public abstract Notification buildNotification();

    protected <T> T getRequiredData(String key) {
        if (!data.containsKey(key)) {
            throw new IllegalArgumentException("Missing required data: " + key);
        }
        return (T) data.get(key);
    }
}