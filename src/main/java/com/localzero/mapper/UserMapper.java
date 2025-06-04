package com.localzero.mapper;

import com.localzero.model.User;
import com.localzero.model.dto.CreateUserRequest;
import com.localzero.model.dto.UserResponse;
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
}
