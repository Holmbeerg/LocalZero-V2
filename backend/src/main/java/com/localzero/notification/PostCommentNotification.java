package com.localzero.notification;

import com.localzero.model.Comment;
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
        getRequiredData("post", Post.class);
        getRequiredData("comment", Comment.class);
        getRequiredData("commenter", User.class);
    }

    @Override
    protected void prepareContent() {
        Post post = getRequiredData("post", Post.class);
        User commenter = getRequiredData("commenter", User.class);

        notification.setTitle("New Comment on Your Post");
        notification.setMessage(String.format(
                "%s commented on your post: %s",
                commenter.getName(),
                post.getText().length() > 50
                        ? post.getText().substring(0, 47) + "..."
                        : post.getText()
        ));
    }

    @Override
    protected void setType() {
        notification.setType(NotificationType.POST_COMMENT);
    }
}