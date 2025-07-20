// In NotificationController.java
package com.localzero.controller;

import com.localzero.model.Notification;
import com.localzero.model.User;
import com.localzero.service.NotificationService;
import com.localzero.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService){
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping
    public List<Notification> getUserNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        return notificationService.getUserNotifications(user);
    }

    @GetMapping("/count")
    public long getUnreadCount(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        return notificationService.getUnreadCount(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        notificationService.deleteNotification(id, user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearAllNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        notificationService.deleteAllUserNotifications(user);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping
//    public List<Notification> getUserNotifications(@AuthenticationPrincipal User user) {
//        return notificationService.getUserNotifications(user);
//    }
//
//    @GetMapping("/count")
//    public long getUnreadCount(@AuthenticationPrincipal User user) {
//        //return notificationService.getUserNotifications(user).size();
//        return notificationService.getUnreadCount(user);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteNotification(
//            @PathVariable Long id,
//            @AuthenticationPrincipal User user
//    ) {
//        notificationService.deleteNotification(id, user);
//        return ResponseEntity.noContent().build();
//    }
//
//    @DeleteMapping
//    public ResponseEntity<Void> clearAllNotifications(@AuthenticationPrincipal User user) {
//        notificationService.deleteAllUserNotifications(user);
//        return ResponseEntity.noContent().build();
//    }
}