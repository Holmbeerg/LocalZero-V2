package com.localzero.service;

import com.localzero.model.Notification;
import com.localzero.model.User;
import com.localzero.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationQueryService {
    private final NotificationRepository repo;
    private final UserService userService;

    public Page<Notification> getMyNotifications(String email, int page, int size) {
        User me = userService.getUserByEmail(email);
        return repo.findByRecipient(me.getUserId(), PageRequest.of(page, size));
    }

    public long getMyUnreadCount(String email) {
        User me = userService.getUserByEmail(email);
        return repo.countUnread(me.getUserId());
    }

    @Transactional
    public void markAsRead(String email, Long id) {
        User me = userService.getUserByEmail(email);
        Notification n = repo.findById(id).orElseThrow();
        if (!n.getRecipient().getUserId().equals(me.getUserId()))
            throw new RuntimeException("Not your notification");
        n.setRead(true);
    }
}
