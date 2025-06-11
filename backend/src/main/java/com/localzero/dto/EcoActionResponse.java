package com.localzero.dto;

import com.localzero.model.enums.EcoActionCategory;

public record EcoActionResponse(
        Long id,
        String date,
        String action,
        EcoActionCategory category,
        Double carbonSaved
) { }
