package com.localzero.mapper;

import com.localzero.dto.*;
import com.localzero.model.InitiativeMember;
import com.localzero.model.User;
import org.springframework.stereotype.Component;

// mapper libraries exist for example MapStruct or ModelMapper, but for simplicity we will do it manually

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getLocation(),
                user.getCreatedAt(),
                user.getRoles().stream().toList()
        );
    }

    public UserSummaryResponse toUserSummaryResponse(User user) {
        return new UserSummaryResponse(
                user.getUserId(),
                user.getName()
        );
    }

    public UserMessageSummaryResponse toUserMessageSummaryResponse(User user) {
        return new UserMessageSummaryResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail()
        );
    }

    public InitiativeParticipantResponse toInitiativeParticipantResponse(InitiativeMember initiativeMember) {
        return new InitiativeParticipantResponse(
                initiativeMember.getUser().getUserId(),
                initiativeMember.getUser().getName(),
                initiativeMember.getJoinedAt().toString()
        );
    }
}
