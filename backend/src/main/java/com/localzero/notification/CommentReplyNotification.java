package com.localzero.notification;

import com.localzero.model.Comment;
import com.localzero.model.Notification;
import com.localzero.model.User;
import com.localzero.model.enums.NotificationType;

import java.util.Map;

public class CommentReplyNotification extends BaseNotification {

    public CommentReplyNotification(Map<String, Object> data) {
        super(data);
    }

    @Override
    protected void validateData() {
        getRequiredData("originalComment");
        getRequiredData("reply");
        getRequiredData("repliedBy");
    }

    @Override
    public Notification create() {
        Comment originalComment = getRequiredData("originalComment");
        Comment reply = getRequiredData("reply");
        User repliedBy = getRequiredData("repliedBy");

        Notification notification = new Notification();
        notification.setType(NotificationType.COMMENT_REPLY);
        notification.setTitle("New Reply to Your Comment");
        notification.setMessage(repliedBy.getName() + " replied to your comment: " + reply.getText());
        notification.setCreatedBy(repliedBy);

        return notification;
    }
}