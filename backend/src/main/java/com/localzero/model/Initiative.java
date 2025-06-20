package com.localzero.model;

import com.localzero.model.enums.InitiativeCategory;
import com.localzero.model.enums.Neighborhood;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "location", nullable = false)
    @ColumnTransformer(write = "?::neighborhood", read = "location::text")
    private Neighborhood location;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    @ColumnTransformer(write = "?::initiative_category",
            read = "category::text")
    // needed to map InitiativeCategory enum to PostgreSQL enum type, unsure if best practice
    private InitiativeCategory category;

    @Column(name = "is_public", nullable = false)
    private boolean publicFlag;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date") // right now, end date is optional
    private LocalDate endDate;

    @OneToMany(mappedBy = "initiative", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<InitiativeMember> participants = new HashSet<>();

    @OneToMany(mappedBy = "initiative", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Post> posts = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
