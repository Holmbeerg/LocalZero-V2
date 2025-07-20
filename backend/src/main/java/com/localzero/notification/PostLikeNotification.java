package com.localzero.notification;

import com.localzero.model.Post;
import com.localzero.model.User;
import com.localzero.model.enums.NotificationType;

import java.util.Map;

public class PostLikeNotification extends BaseNotification {

    public PostLikeNotification(Map<String, Object> data) {
        super(data);
    }

    @Override
    protected void validateData() {
        getRequiredData("post", Post.class);
        getRequiredData("likedBy", User.class);
    }

    @Override
    protected void prepareContent() {
        Post post = getRequiredData("post", Post.class);
        User likedBy = getRequiredData("likedBy", User.class);

        notification.setTitle("New Like on Your Post");
        notification.setMessage(String.format(
                "%s liked your post: %s",
                likedBy.getName(),
                post.getText().length() > 50
                        ? post.getText().substring(0, 47) + "..."
                        : post.getText()
        ));
    }

    @Override
    protected void setType() {
        notification.setType(NotificationType.NEW_LIKE_ON_POST);
    }
}