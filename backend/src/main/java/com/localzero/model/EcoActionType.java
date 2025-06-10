package com.localzero.model;

import com.localzero.model.enums.EcoActionCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "eco_action_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EcoActionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="action", nullable = false)
    private String action;

    @Column(name="carbon_savings_kg", nullable = false)
    private Double carbonSaved;

    @Enumerated(EnumType.STRING)
    @Column(name="category", nullable = false)
    private EcoActionCategory category;
}