package com.localzero.mapper;

import com.localzero.dto.InitiativeDetailResponse;
import com.localzero.dto.InitiativeParticipantResponse;
import com.localzero.dto.PostSummaryResponse;
import com.localzero.model.Initiative;
import com.localzero.model.User;
import org.springframework.stereotype.Component;
import com.localzero.dto.InitiativeSummaryResponse;

import java.time.LocalDate;
import java.util.List;

@Component
public class InitiativeMapper {

    private final UserMapper userMapper;
    private final PostMapper postMapper;

    public InitiativeMapper(UserMapper userMapper, PostMapper postMapper) {
        this.userMapper = userMapper;
        this.postMapper = postMapper;
    }


    public InitiativeSummaryResponse toResponse(Initiative initiative, User currentUser) {
        boolean isUserCreator = isUserCreator(initiative, currentUser);
        boolean isUserParticipant = isUserCreator || isUserParticipant(initiative, currentUser);

        return InitiativeSummaryResponse.builder()
                .id(initiative.getId())
                .title(initiative.getTitle())
                .description(initiative.getDescription())
                .creator(userMapper.toUserSummaryResponse(initiative.getCreator()))
                .location(initiative.getLocation())
                .category(initiative.getCategory())
                .isPublic(initiative.isPublicFlag())
                .participantsCount(initiative.getParticipants().size())
                .startDate(formatDate(initiative.getStartDate()))
                .endDate(formatDate(initiative.getEndDate()))
                .isUserParticipant(isUserParticipant)
                .isUserCreator(isUserCreator)
                .build();
    }

    public InitiativeDetailResponse toDetailResponse(Initiative initiative, User currentUser) {
        boolean isUserCreator = isUserCreator(initiative, currentUser);
        boolean isUserParticipant = isUserCreator || isUserParticipant(initiative, currentUser);

        return InitiativeDetailResponse.builder()
                .id(initiative.getId())
                .title(initiative.getTitle())
                .description(initiative.getDescription())
                .creator(userMapper.toUserSummaryResponse(initiative.getCreator()))
                .location(initiative.getLocation())
                .category(initiative.getCategory())
                .isPublic(initiative.isPublicFlag())
                .participantsCount(initiative.getParticipants().size())
                .startDate(formatDate(initiative.getStartDate()))
                .endDate(formatDate(initiative.getEndDate()))
                .isUserParticipant(isUserParticipant)
                .isUserCreator(isUserCreator)
                .participants(mapParticipants(initiative))
                .posts(mapPosts(initiative, currentUser))
                .build();
    }

    private boolean isUserCreator(Initiative initiative, User currentUser) {
        return initiative.getCreator().getUserId().equals(currentUser.getUserId());
    }

    private boolean isUserParticipant(Initiative initiative, User currentUser) {
        return initiative.getParticipants().stream()
                .anyMatch(im -> im.getUser().getUserId().equals(currentUser.getUserId()));
    }

    private List<InitiativeParticipantResponse> mapParticipants(Initiative initiative) {
        return initiative.getParticipants().stream()
                .map(userMapper::toInitiativeParticipantResponse)
                .toList();
    }

    private List<PostSummaryResponse> mapPosts(Initiative initiative, User user) {
        return initiative.getPosts().stream()
                .map(post -> postMapper.toPostSummaryResponse(post, user))
                .toList();
    }

    private String formatDate(LocalDate dateTime) {
        return dateTime != null ? dateTime.toString() : null;
    }
}
