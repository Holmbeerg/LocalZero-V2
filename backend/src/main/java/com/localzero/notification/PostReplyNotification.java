package com.localzero.notification;

import com.localzero.model.Notification;
import com.localzero.model.enums.NotificationType;

import java.util.Map;

public class PostReplyNotification extends BaseNotification {
    public PostReplyNotification(Map<String, Object> data) {
        super(data);
    }

    @Override
    protected void validateData() {
        getRequiredData("postText");
        getRequiredData("repliedBy");
    }

    @Override
    public Notification create() {
        String postText = getRequiredData("postText").toString();
        String repliedBy = getRequiredData("repliedBy").toString();

        Notification notification = new Notification();
        notification.setType(NotificationType.COMMENT_REPLY);
        notification.setTitle("New Reply to Your Post");
        
        String truncatedPost = postText.length() > 50 
            ? postText.substring(0, 50) + "..." 
            : postText;
            
        notification.setMessage(repliedBy + " replied to your post: \"" + truncatedPost + "\"");
        return notification;
    }
}