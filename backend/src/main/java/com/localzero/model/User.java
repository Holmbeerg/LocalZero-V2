package com.localzero.model;

import com.localzero.model.enums.Neighborhood;
import com.localzero.model.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Relies on the database’s auto-increment feature.
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email", nullable = false, unique = true) // these should match the database constraints
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "location", nullable = false)
    @ColumnTransformer(write = "?::neighborhood")
    private Neighborhood location;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER) // EAGER fetch type to load roles immediately with the user
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    public boolean hasRole(RoleName roleName) {
        return roles.stream()
                .anyMatch(role -> role.getRoleName() == roleName);
    }

    public void removeRole(RoleName roleName) {
        roles.removeIf(role -> role.getRoleName() == roleName);
    }

    public Set<GrantedAuthority> getAuthorities() { //
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return email;
    }
}