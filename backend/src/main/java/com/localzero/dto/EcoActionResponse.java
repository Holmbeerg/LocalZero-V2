package com.localzero.dto;

import com.localzero.model.enums.EcoActionCategory;

import java.math.BigDecimal;

public record EcoActionResponse(
        Long id,
        String date,
        String action,
        EcoActionCategory category,
        BigDecimal carbonSaved
) { }
