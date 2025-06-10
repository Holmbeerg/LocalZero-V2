package com.localzero.mapper;

import com.localzero.model.Initiative;
import org.springframework.stereotype.Component;
import com.localzero.model.dto.InitiativeResponse;

@Component
public class InitiativeMapper {

    private final UserMapper userMapper;

    public InitiativeMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public InitiativeResponse toResponse(Initiative initiative) {
        return new InitiativeResponse(
                initiative.getId(),
                initiative.getTitle(),
                initiative.getDescription(),
                userMapper.toUserSummaryResponse(initiative.getCreator()),
                initiative.getLocation(),
                initiative.getCategory(),
                initiative.isPublic(),
                initiative.getStartDate().toString(),
                initiative.getEndDate() != null ? initiative.getEndDate().toString() : null
        );
    }
}
