package com.localzero.model;

import com.localzero.model.enums.EcoActionCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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

    @NotBlank
    @Column(name="action", nullable = false)
    private String action;

    @NotNull
    @Column(name="carbon_savings_kg", nullable = false)
    private BigDecimal carbonSaved;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="category", nullable = false)
    private EcoActionCategory category;
}