package com.localzero.controller;

import com.localzero.dto.NotificationSummaryResponse;
import com.localzero.mapper.NotificationMapper;
import com.localzero.service.NotificationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationQueryService queryService;
    private final NotificationMapper mapper;

    @GetMapping
    public Page<NotificationSummaryResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal) {

        return queryService.getMyNotifications(principal.getName(), page, size)
                .map(mapper::toNotificationSummaryResponse);
    }

    @GetMapping("/unread-count")
    public Map<String, Long> unreadCount(Principal principal) {
        long c = queryService.getMyUnreadCount(principal.getName());
        return Map.of("count", c);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable Long id, Principal principal) {
        queryService.markAsRead(principal.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
