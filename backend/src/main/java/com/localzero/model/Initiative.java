package com.localzero.model;

import com.localzero.model.enums.InitiativeCategory;
import com.localzero.model.enums.Neighborhood;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "initiatives")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Initiative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "location", nullable = false)
    @ColumnTransformer(write = "?::neighborhood") // maps Neighborhood enum to PostgreSQL enum type
    private Neighborhood location;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    @ColumnTransformer(write = "?::initiative_category")
    // needed to map InitiativeCategory enum to PostgreSQL enum type, unsure if best practice
    private InitiativeCategory category;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date") // right now, end date is optional
    private LocalDate endDate;

    @ManyToMany
    @JoinTable(
            name = "initiative_members",
            joinColumns = @JoinColumn(name = "initiative_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )

    @Builder.Default
    private Set<User> participants = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
