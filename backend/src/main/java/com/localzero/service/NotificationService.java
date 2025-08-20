package com.localzero.service;

import com.localzero.model.Notification;
import com.localzero.model.User;
import com.localzero.model.UserNotification;
import com.localzero.model.enums.NotificationType;
import com.localzero.notification.BaseNotification;
import com.localzero.notification.NotificationFactory;
import com.localzero.repository.NotificationRepository;
import com.localzero.repository.UserNotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final NotificationFactory notificationFactory;

    public Page<Notification> getUserNotifications(User user, Pageable pageable) {
        Page<Long> notificationIds = notificationRepository.findNotificationIdsByUser(user, pageable);

        if (notificationIds.getContent().isEmpty()) {
            return new PageImpl<>(List.of(), pageable, notificationIds.getTotalElements());
        }

        List<Notification> notifications = notificationRepository.findNotificationsByIds(notificationIds.getContent());
        return new PageImpl<>(notifications, pageable, notificationIds.getTotalElements());
    }

    public Notification getNotification(Long id, User user) {
        return userNotificationRepository.findByUserIdAndNotificationId(user.getUserId(), id)
                .map(UserNotification::getNotification)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
    }

    public long countUnreadNotifications(User user) {
        return userNotificationRepository.countByUserId(user.getUserId());
    }

    @Transactional
    public void deleteNotification(Long notificationId, User user) {
        if (!userNotificationRepository.existsByUserIdAndNotificationId(user.getUserId(), notificationId)) {
            log.warn("User {} attempted to delete non-existent or unauthorized notification {}", 
                    user.getUserId(), notificationId);
            return;
        }
        userNotificationRepository.deleteByNotificationIdAndUserId(notificationId, user.getUserId());
        log.info("Deleted notification {} for user {}", notificationId, user.getUserId());
    }

    @Transactional
    public void deleteAllNotifications(User user) {
        userNotificationRepository.deleteByUserId(user.getUserId());
    }

    @Transactional
    public void createAndAssignNotification(NotificationType type, User recipient, User createdBy, Map<String, Object> data) {
        BaseNotification notificationBuilder = notificationFactory.createNotification(type, data);
        Notification notification = notificationBuilder.createNotification();
        notification.setCreatedBy(createdBy);
        notificationRepository.save(notification);
        assignNotificationToUser(notification, recipient);
    }

    @Transactional
    public void createAndAssignNotification(NotificationType type, List<User> recipients, User createdBy, Map<String, Object> data) {
        BaseNotification notificationBuilder = notificationFactory.createNotification(type, data);
        Notification notification = notificationBuilder.createNotification();
        notification.setCreatedBy(createdBy);
        notificationRepository.save(notification);
        assignNotificationToUsers(notification, recipients);
    }

    @Transactional
    public void assignNotificationToUser(Notification notification, User user) {
        if (userNotificationRepository.existsByUserIdAndNotificationId(user.getUserId(), notification.getId())) {
            log.debug("Notification {} already assigned to user {}", notification.getId(), user.getUserId());
            return;
        }
        
        UserNotification userNotification = new UserNotification();
        userNotification.setUser(user);
        userNotification.setNotification(notification);
        userNotificationRepository.save(userNotification);
        log.info("Assigned notification {} to user {}", notification.getId(), user.getUserId());
    }

    @Transactional
    public void assignNotificationToUsers(Notification notification, List<User> users) {
        if (users == null || users.isEmpty()) {
            log.warn("No users provided to assign notification {}", notification.getId());
            return;
        }

        int assignedCount = 0;
        for (User user : users) {
            if (user != null && !userNotificationRepository.existsByUserIdAndNotificationId(user.getUserId(), notification.getId())) {
                assignNotificationToUser(notification, user);
                assignedCount++;
            }
        }
        
        log.info("Assigned notification {} to {} users", notification.getId(), assignedCount);
    }
}
