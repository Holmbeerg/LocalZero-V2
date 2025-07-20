package com.localzero.repository;

import com.localzero.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Basic CRUD operations are provided by JpaRepository
}