package com.localzero.repository;

import com.localzero.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
      select n from Notification n
      where n.recipient.userId = :userId
      order by n.createdAt desc
    """)
    Page<Notification> findByRecipient(@Param("userId") Long userId, Pageable pageable);

    @Query("""
      select count(n) from Notification n
      where n.recipient.userId = :userId and n.read = false
    """)
    long countUnread(@Param("userId") Long userId);
}
