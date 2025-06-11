package com.localzero.dto;

import com.localzero.model.enums.InitiativeCategory;
import com.localzero.model.enums.Neighborhood;

public record InitiativeResponse(
    Long id,
    String title,
    String description,
    UserSummaryResponse creator,
    Neighborhood location,
    InitiativeCategory category,
    boolean isPublic,
    int participantsCount,
    String startDate,
    String endDate
) { }
