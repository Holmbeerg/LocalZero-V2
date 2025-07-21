package com.localzero.service;

import com.localzero.model.Notification;
import com.localzero.model.User;
import com.localzero.model.UserNotification;
import com.localzero.model.enums.NotificationType;
import com.localzero.notification.BaseNotification;
import com.localzero.notification.NotificationFactory;
import com.localzero.repository.NotificationRepository;
import com.localzero.repository.UserNotificationRepository;
import com.localzero.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final NotificationFactory notificationFactory;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserNotificationRepository userNotificationRepository,
                               NotificationFactory notificationFactory, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userNotificationRepository = userNotificationRepository;
        this.notificationFactory = notificationFactory;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createAndAssignNotification(NotificationType type, Map<String, Object> data, User recipient) {
        BaseNotification notificationBuilder = notificationFactory.createNotification(type, data);
        Notification notification = notificationBuilder.create();

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication() != null ?
                SecurityContextHolder.getContext().getAuthentication().getName() :
                null;

        if (notification.getCreatedBy() == null && currentUserEmail != null) {
            try {
                User currentUser = userRepository.findByEmail(currentUserEmail)
                        .orElse(null);
                if (currentUser != null) {
                    notification.setCreatedBy(currentUser);
                }
            } catch (Exception e) {
                log.warn("Could not find user with email: {}", currentUserEmail, e);
            }
        }

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
        final Notification notification = notificationBuilder.create();

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication() != null ?
                SecurityContextHolder.getContext().getAuthentication().getName() :
                null;

        if (notification.getCreatedBy() == null && currentUserEmail != null) {
            try {
                User currentUser = userRepository.findByEmail(currentUserEmail)
                        .orElse(null);
                if (currentUser != null) {
                    notification.setCreatedBy(currentUser);
                }
            } catch (Exception e) {
                log.warn("Could not find user with email: {}", currentUserEmail, e);
            }
        }

        final Notification savedNotification = notificationRepository.save(notification);

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