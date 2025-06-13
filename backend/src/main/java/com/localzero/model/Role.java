package com.localzero.model;

import com.localzero.model.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false, unique = true)
    @ColumnTransformer(write = "?::user_role", // PostgreSQL specific type casting
            read = "role_name::text")
    private RoleName roleName;

    @Override
    public String toString() {
        return roleName != null ? roleName.name() : "UNKNOWN";
    }
}