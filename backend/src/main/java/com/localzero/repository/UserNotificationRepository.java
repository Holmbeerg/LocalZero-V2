package com.localzero.repository;

import com.localzero.model.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    
    @Query("SELECT un FROM UserNotification un " +
           "JOIN FETCH un.notification n " +
           "WHERE un.user.userId = :userId " +
           "ORDER BY n.createdAt DESC")
    List<UserNotification> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(un) > 0 FROM UserNotification un " +
           "WHERE un.user.userId = :userId AND un.notification.id = :notificationId")
    boolean existsByUserIdAndNotificationId(
            @Param("userId") Long userId,
            @Param("notificationId") Long notificationId
    );
    
    @Modifying
    @Query("DELETE FROM UserNotification un " +
           "WHERE un.notification.id = :notificationId AND un.user.userId = :userId")
    void deleteByNotificationIdAndUserId(
            @Param("notificationId") Long notificationId,
            @Param("userId") Long userId
    );
    
    @Query("SELECT COUNT(un) FROM UserNotification un " +
           "WHERE un.user.userId = :userId")
    long countByUserId(@Param("userId") Long userId);
    
    @Modifying
    @Query("DELETE FROM UserNotification un WHERE un.user.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
    
    @Modifying
    @Query("DELETE FROM UserNotification un WHERE un.notification.id = :notificationId")
    void deleteByNotificationId(@Param("notificationId") Long notificationId);

    @Query("SELECT un FROM UserNotification un " +
            "JOIN FETCH un.notification n " +
            "WHERE un.user.userId = :userId AND un.notification.id = :notificationId")
    Optional<UserNotification> findByUserIdAndNotificationId(
            @Param("userId") Long userId,
            @Param("notificationId") Long notificationId
    );
}