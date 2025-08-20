package com.localzero.notification;

import com.localzero.model.Notification;
import com.localzero.model.enums.NotificationType;

import java.util.Map;

public class NewPostNotification extends BaseNotification {
    public NewPostNotification(Map<String, Object> data) {
        super(data);
    }

    @Override
    protected void validateData() {
        getRequiredData("initiativeTitle");
        getRequiredData("postText");
        getRequiredData("postBy");
    }

    @Override
    public Notification buildNotification() {
        String initiativeTitle = getRequiredData("initiativeTitle");
        String postText = getRequiredData("postText");
        String postByName = getRequiredData("postBy");

        Notification notification = new Notification();
        notification.setType(NotificationType.NEW_POST_IN_INITIATIVE);
        notification.setTitle("New Post In Your Initiative");

        String truncatedInitiativeTitle = initiativeTitle.length() > 30
                ? initiativeTitle.substring(0, 30) + "..."
                : initiativeTitle;
        String truncatedPostText = postText.length() > 50
                ? postText.substring(0, 50) + "..."
                : postText;

        notification.setMessage(postByName + " created a post in your initiative \"" +
                truncatedInitiativeTitle + "\": " + truncatedPostText);

        return notification;
    }
}