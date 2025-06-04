package com.localzero.model;

import com.localzero.model.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
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

    @Column(name = "location")
    private String location;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER) // EAGER fetch type to load roles immediately with the user
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    // Set createdAt only on insert
    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }


    public void addRole(Role role) {
        this.roles.add(role);
    }

    public boolean hasRole(RoleName roleName) {
        return roles.stream()
                .anyMatch(role -> role.getRoleName() == roleName);
    }

    public void removeRole(RoleName roleName) {
        roles.removeIf(role -> role.getRoleName() == roleName);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() { // TODO: This method is used by Spring Security to get the roles of the user, needed for authentication and authorization?
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return email;
    }
}