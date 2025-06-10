package com.localzero.model.dto;

import com.localzero.model.enums.InitiativeCategory;

public record InitiativeResponse(
    Long id,
    String title,
    String description,
    UserSummaryResponse creator,
    String location,
    InitiativeCategory category,
    boolean isPublic,
    String startDate,
    String endDate
) { }
