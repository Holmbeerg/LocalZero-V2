package com.localzero.notification;

import com.localzero.model.Notification;
import com.localzero.model.enums.NotificationType;

import java.util.Map;

//TODO: finish refactor
public class NewPostNotification extends BaseNotification {
    public NewPostNotification(Map<String, Object> data) {
        super(data);
    }

    @Override
    protected void validateData() {
        getRequiredData("postText");
        getRequiredData("commentText");
        getRequiredData("commentedBy");
    }

    @Override
    public Notification create() {
        String postText = getRequiredData("postText").toString();
        String commentText = getRequiredData("commentText").toString();
        String commentedByName = getRequiredData("commentedBy").toString();

        Notification notification = new Notification();
        notification.setType(NotificationType.NEW_POST_IN_INITIATIVE);
        notification.setTitle("New Post In Your Initiative");

        String truncatedPost = postText.length() > 30
                ? postText.substring(0, 30) + "..."
                : postText;
        String truncatedComment = commentText.length() > 50
                ? commentText.substring(0, 50) + "..."
                : commentText;

        notification.setMessage(commentedByName + " Created a post in your initiative\"" +
                truncatedPost + "\": " + truncatedComment);

        return notification;
    }
}