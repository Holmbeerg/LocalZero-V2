package com.localzero.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

// Represents the composite key for InitiativeMember entity
public class InitiativeMemberId implements Serializable {

    @Column(name = "initiative_id", nullable = false)
    private Long initiativeId;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
