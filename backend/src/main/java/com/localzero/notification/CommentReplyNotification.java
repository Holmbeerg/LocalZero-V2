package com.localzero.notification;

import com.localzero.model.Comment;
import com.localzero.model.User;
import com.localzero.model.enums.NotificationType;

import java.util.Map;

public class CommentReplyNotification extends BaseNotification {

    public CommentReplyNotification(Map<String, Object> data) {
        super(data);
    }

    @Override
    protected void validateData() {
        getRequiredData("parentComment", Comment.class);
        getRequiredData("reply", Comment.class);
        getRequiredData("replier", User.class);
    }

    @Override
    protected void prepareContent() {
        Comment parentComment = getRequiredData("parentComment", Comment.class);
        User replier = getRequiredData("replier", User.class);

        notification.setTitle("New Reply to Your Comment");
        notification.setMessage(String.format(
                "%s replied to your comment: %s",
                replier.getName(),
                parentComment.getText().length() > 50
                        ? parentComment.getText().substring(0, 47) + "..."
                        : parentComment.getText()
        ));
    }

    @Override
    protected void setType() {
        notification.setType(NotificationType.COMMENT_REPLY);
    }
}