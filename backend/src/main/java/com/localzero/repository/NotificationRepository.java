package com.localzero.repository;

import com.localzero.model.Notification;
import com.localzero.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n.id FROM Notification n " +
            "JOIN n.userNotifications un " +
            "WHERE un.user = :user " +
            "ORDER BY n.createdAt DESC")
    Page<Long> findNotificationIdsByUser(@Param("user") User user, Pageable pageable);

    @EntityGraph(attributePaths = {"userNotifications", "userNotifications.user"})
    @Query("SELECT n FROM Notification n WHERE n.id IN :ids ORDER BY n.createdAt DESC")
    List<Notification> findNotificationsByIds(@Param("ids") List<Long> ids);
}