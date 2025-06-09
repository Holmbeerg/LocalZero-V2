package com.localzero.model.dto;

public record EcoActionResponse(
        Long id,
        String date,
        String action,
        String category,
        Double carbonSaved
) { }
