package com.localzero.notification;

import com.localzero.model.Notification;
import com.localzero.model.enums.NotificationType;

import java.util.Map;

public class PostCommentNotification extends BaseNotification {
    public PostCommentNotification(Map<String, Object> data) {
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
        notification.setType(NotificationType.NEW_COMMENT_ON_POST);
        notification.setTitle("New Comment on Your Post");

        String truncatedPost = postText.length() > 30
                ? postText.substring(0, 30) + "..."
                : postText;
        String truncatedComment = commentText.length() > 50
                ? commentText.substring(0, 50) + "..."
                : commentText;

        notification.setMessage(commentedByName + " commented on your post \"" +
                truncatedPost + "\": " + truncatedComment);

        return notification;
    }
}