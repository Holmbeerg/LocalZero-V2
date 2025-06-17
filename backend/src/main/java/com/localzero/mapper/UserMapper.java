package com.localzero.mapper;

import com.localzero.dto.InitiativeParticipantResponse;
import com.localzero.model.InitiativeMember;
import com.localzero.model.User;
import com.localzero.dto.CreateUserRequest;
import com.localzero.dto.UserResponse;
import com.localzero.dto.UserSummaryResponse;
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

    public User toUser(CreateUserRequest request) {
        return User.builder()
                .email(request.email())
                .passwordHash(request.password())
                .name(request.name())
                .location(request.location())
                .build();
    }

    public UserSummaryResponse toUserSummaryResponse(User user) {
        return new UserSummaryResponse(
                user.getUserId(),
                user.getName()
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
