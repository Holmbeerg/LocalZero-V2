package com.localzero.repository;

import com.localzero.model.Notification;
import com.localzero.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT DISTINCT n FROM Notification n " +
            "LEFT JOIN FETCH n.userNotifications un " +
            "LEFT JOIN FETCH un.user u " +
            "WHERE u = :user " +
            "ORDER BY n.createdAt DESC")
    Page<Notification> findByUser(@Param("user") User user, Pageable pageable);
}