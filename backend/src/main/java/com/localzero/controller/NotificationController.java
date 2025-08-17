package com.localzero.controller;

import com.localzero.dto.CreateNotificationRequest;
import com.localzero.dto.NotificationResponse;
import com.localzero.dto.NotificationSummaryResponse;
import com.localzero.mapper.NotificationMapper;
import com.localzero.model.Notification;
import com.localzero.model.User;
import com.localzero.model.UserNotification;
import com.localzero.repository.NotificationRepository;
import com.localzero.repository.UserNotificationRepository;
import com.localzero.service.NotificationService;
import com.localzero.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getNotifications(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        User user = userService.getUserByEmail(userDetails.getUsername());
        Page<Notification> notificationsPage = notificationService.getUserNotifications(user, PageRequest.of(page, size));

        List<NotificationSummaryResponse> content = notificationsPage.getContent().stream()
                .map(notificationMapper::toSummaryResponse)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("content", content);
        response.put("totalElements", notificationsPage.getTotalElements());

        Map<String, Object> data = new HashMap<>();
        data.put("data", response);

        return ResponseEntity.ok(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotification(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        Notification notification = notificationService.getNotification(id, user);
        return ResponseEntity.ok(notificationMapper.toResponse(notification));
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getUnreadCount(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        long count = notificationService.countUnreadNotifications(user);

        Map<String, Object> data = new HashMap<>();
        data.put("count", count);

        return ResponseEntity.ok(data);
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
    public ResponseEntity<Void> clearAllNotifications(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        notificationService.deleteAllNotifications(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<?> createNotification(
            @Valid @RequestBody CreateNotificationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User createdBy = userService.getUserByEmail(userDetails.getUsername());
            User recipient = userService.getUserById(request.getRecipientId());

            notificationService.createAndAssignNotification(
                    request.getType(),
                    recipient,
                    createdBy,
                    Map.of("title", request.getTitle(), "message", request.getMessage())
            );

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Recipient not found"));
        } catch (Exception e) {
            log.error("Failed to create notification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create notification"));
        }
    }
}