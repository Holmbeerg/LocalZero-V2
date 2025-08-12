package com.localzero.notification;

import com.localzero.model.Comment;
import com.localzero.model.Notification;
import com.localzero.model.Post;
import com.localzero.model.User;
import com.localzero.model.enums.NotificationType;

import java.util.Map;

public class PostCommentNotification extends BaseNotification {

    public PostCommentNotification(Map<String, Object> data) {
        super(data);
    }

    @Override
    protected void validateData() {
        getRequiredData("post");
        getRequiredData("comment");
        getRequiredData("commentedBy");
    }

    @Override
    public Notification create() {
        Post post = getRequiredData("post");
        Comment comment = getRequiredData("comment");
        User commentedBy = getRequiredData("commentedBy");

        Notification notification = new Notification();
        notification.setType(NotificationType.POST_COMMENT);
        notification.setTitle("New Comment on Your Post");
        notification.setMessage(commentedBy.getName() + " commented: " + comment.getText());
        notification.setCreatedBy(commentedBy);

        return notification;
    }

}