package com.localzero.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "initiative_members")
public class InitiativeMember {

    @EmbeddedId
    private InitiativeMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("initiativeId") //
    @JoinColumn(name = "initiative_id", nullable = false)
    private Initiative initiative;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "joined_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime joinedAt;

    public InitiativeMember(Initiative initiative, User user) {
        this.id = new InitiativeMemberId(initiative.getId(), user.getUserId());
        this.initiative = initiative;
        this.user = user;
    }
}
