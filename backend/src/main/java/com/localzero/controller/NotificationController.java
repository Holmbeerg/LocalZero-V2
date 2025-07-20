// In NotificationController.java
package com.localzero.controller;

import com.localzero.model.Notification;
import com.localzero.model.User;
import com.localzero.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public List<Notification> getUserNotifications(@AuthenticationPrincipal User user) {
        return notificationService.getUserNotifications(user);
    }

    @GetMapping("/count")
    public long getUnreadCount(@AuthenticationPrincipal User user) {
        return notificationService.getUserNotifications(user).size();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        notificationService.deleteNotification(id, user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearAllNotifications(@AuthenticationPrincipal User user) {
        notificationService.deleteAllUserNotifications(user);
        return ResponseEntity.noContent().build();
    }
}