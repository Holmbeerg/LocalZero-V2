package com.localzero.notification;

import com.localzero.model.Notification;
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
        getRequiredData("post");
        getRequiredData("likedBy");
    }

    @Override
    public Notification create() {
        Post post = getRequiredData("post");
        User likedBy = getRequiredData("likedBy");

        Notification notification = new Notification();
        notification.setType(NotificationType.NEW_LIKE_ON_POST);
        notification.setTitle("New Like on Your Post");
        notification.setMessage(likedBy.getName() + " liked your post");
        notification.setCreatedBy(likedBy);

        return notification;
    }
}