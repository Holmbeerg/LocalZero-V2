package com.localzero.notification;

import com.localzero.model.Notification;
import com.localzero.model.enums.NotificationType;

import java.util.Map;

public class PostLikeNotification extends BaseNotification {

    public PostLikeNotification(Map<String, Object> data) {
        super(data);
    }

    @Override
    protected void validateData() {
        getRequiredData("postText");
        getRequiredData("likedBy");
    }

    @Override
    public Notification buildNotification() {
        String postText = getRequiredData("postText").toString();
        String likedBy = getRequiredData("likedBy").toString();

        Notification notification = new Notification();
        notification.setType(NotificationType.NEW_LIKE_ON_POST);
        notification.setTitle("New Like on Your Post");

        String truncatedText = postText.length() > 50
                ? postText.substring(0, 50) + "..."
                : postText;
        notification.setMessage(likedBy + " liked your post: " + truncatedText);

        return notification;
    }
}