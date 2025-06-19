package com.localzero.mapper;

import com.localzero.dto.InitiativeDetailResponse;
import com.localzero.model.Initiative;
import com.localzero.model.User;
import org.springframework.stereotype.Component;
import com.localzero.dto.InitiativeResponse;

@Component
public class InitiativeMapper {

    private final UserMapper userMapper;

    public InitiativeMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    public InitiativeResponse toResponse(Initiative initiative, User currentUser) {
        boolean isUserCreator = isUserCreator(initiative, currentUser);
        boolean isUserParticipant = isUserCreator || isUserParticipant(initiative, currentUser);

        return new InitiativeResponse(
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

    public InitiativeDetailResponse toDetailResponse(Initiative initiative, User currentUser) {
        boolean isUserCreator = isUserCreator(initiative, currentUser);
        boolean isUserParticipant = isUserCreator || isUserParticipant(initiative, currentUser);

        return new InitiativeDetailResponse(
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
                isUserCreator,
                initiative.getParticipants().stream()
                        .map(userMapper::toInitiativeParticipantResponse)
                        .toList()
        );
    }

    private boolean isUserCreator(Initiative initiative, User currentUser) {
        return initiative.getCreator().getUserId().equals(currentUser.getUserId());
    }

    private boolean isUserParticipant(Initiative initiative, User currentUser) {
        return initiative.getParticipants().stream()
                .anyMatch(im -> im.getUser().getUserId().equals(currentUser.getUserId()));
    }
}
