package com.localzero.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_notification",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_user_notification",
        columnNames = {"user_id", "notification_id"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_notification_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notification_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_notification_notification"))
    private Notification notification;
}