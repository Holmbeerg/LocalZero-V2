package com.localzero.mapper;

import com.localzero.model.Initiative;
import com.localzero.model.User;
import org.springframework.stereotype.Component;
import com.localzero.dto.InitiativeListResponse;

@Component
public class InitiativeMapper {

    private final UserMapper userMapper;

    public InitiativeMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public InitiativeListResponse toResponse(Initiative initiative, User currentUser) {
        boolean isUserCreator = initiative.getCreator().getUserId().equals(currentUser.getUserId());
        boolean isUserParticipant = isUserCreator || initiative.getParticipants().stream()
                .anyMatch(u -> u.getUserId().equals(currentUser.getUserId()));

        return new InitiativeListResponse(
                initiative.getId(),
                initiative.getTitle(),
                initiative.getDescription(),
                userMapper.toUserSummaryResponse(initiative.getCreator()),
                initiative.getLocation(),
                initiative.getCategory(),
                initiative.isPublicFlag(),
                initiative.getParticipants().size(),
                initiative.getStartDate().toString(),
                initiative.getEndDate() != null ? initiative.getEndDate().toString() : null,
                isUserParticipant,
                isUserCreator
        );
    }
}
