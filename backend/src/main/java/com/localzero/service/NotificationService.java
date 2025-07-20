package com.localzero.service;

import com.localzero.model.Notification;
import com.localzero.model.User;
import com.localzero.model.UserNotification;
import com.localzero.model.enums.NotificationType;
import com.localzero.notification.BaseNotification;
import com.localzero.notification.NotificationFactory;
import com.localzero.repository.NotificationRepository;
import com.localzero.repository.UserNotificationRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final NotificationFactory notificationFactory;

    public NotificationService(NotificationRepository notificationRepository, UserNotificationRepository userNotificationRepository,
                               NotificationFactory notificationFactory) {
        this.notificationRepository = notificationRepository;
        this.userNotificationRepository = userNotificationRepository;
        this.notificationFactory = notificationFactory;
    }

    @Transactional
    public void createAndAssignNotification(NotificationType type, Map<String, Object> data, User recipient) {
        BaseNotification notificationBuilder = notificationFactory.createNotification(type, data);
        Notification notification = notificationBuilder.create();

        notification = notificationRepository.save(notification);

        UserNotification userNotification = new UserNotification();
        userNotification.setUser(recipient);
        userNotification.setNotification(notification);
        userNotificationRepository.save(userNotification);
    }

    @Transactional
    public void createAndAssignNotification(NotificationType type, Map<String, Object> data, List<User> recipients) {
        if (recipients == null || recipients.isEmpty()) {
            return;
        }

        BaseNotification notificationBuilder = notificationFactory.createNotification(type, data);
        final Notification savedNotification = notificationRepository.save(notificationBuilder.create());

        List<UserNotification> userNotifications = recipients.stream()
                .filter(recipient -> recipient != null && !recipient.equals(savedNotification.getCreatedBy()))
                .map(recipient -> {
                    UserNotification un = new UserNotification();
                    un.setUser(recipient);
                    un.setNotification(savedNotification);
                    return un;
                })
                .toList();

        userNotificationRepository.saveAll(userNotifications);
    }

    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(User user) {
        return userNotificationRepository.findByUserIdOrderByNotificationCreatedAtDesc(user.getUserId()).stream()
                .map(UserNotification::getNotification)
                .toList();
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(User user) {
        return userNotificationRepository.countByUserId(user.getUserId());
    }

    @Transactional
    public void deleteNotification(Long notificationId, User user) {
        userNotificationRepository.deleteByUserIdAndNotificationId(user.getUserId(), notificationId);
    }

    @Transactional
    public void deleteAllUserNotifications(User user) {
        userNotificationRepository.deleteByUserId(user.getUserId());
    }
}