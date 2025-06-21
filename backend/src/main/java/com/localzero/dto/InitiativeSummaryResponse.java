package com.localzero.dto;

import com.localzero.model.enums.InitiativeCategory;
import com.localzero.model.enums.Neighborhood;
import lombok.Builder;

@Builder
public record InitiativeSummaryResponse(
    Long id,
    String title,
    String description,
    UserSummaryResponse creator,
    Neighborhood location,
    InitiativeCategory category,
    boolean isPublic,
    int participantsCount,
    String startDate,
    String endDate,
    boolean isUserParticipant,
    boolean isUserCreator
) { }
