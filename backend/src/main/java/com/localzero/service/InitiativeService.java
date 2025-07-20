package com.localzero.service;

import com.localzero.exception.AlreadyInitiativeMemberException;
import com.localzero.exception.InitiativeNotFoundException;
import com.localzero.exception.PostNotFoundException;
import com.localzero.model.*;
import com.localzero.dto.CreateInitiativeRequest;
import com.localzero.model.enums.NotificationType;
import com.localzero.repository.InitiativeMemberRepository;
import com.localzero.repository.InitiativeRepository;
import com.localzero.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Transactional
@Service
@Slf4j
public class InitiativeService {
    private final InitiativeRepository initiativeRepository;
    private final InitiativeMemberRepository initiativeMemberRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;
    private final UserService userService;

    public InitiativeService(InitiativeRepository initiativeRepository, InitiativeMemberRepository initiativeMemberRepository,
                            PostRepository postRepository, NotificationService notificationService, UserService userService) {
        this.initiativeRepository = initiativeRepository;
        this.initiativeMemberRepository = initiativeMemberRepository;
        this.postRepository = postRepository;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    public Initiative createInitiative(CreateInitiativeRequest initiativeRequest, User user) {
        log.info("Creating initiative for user: {} with title: {}", user.getEmail(), initiativeRequest.title());

        Initiative initiative = Initiative.builder()
                .title(initiativeRequest.title())
                .description(initiativeRequest.description())
                .creator(user)
                .location(user.getLocation())
                .category(initiativeRequest.category())
                .publicFlag(initiativeRequest.isPublic())
                .startDate(initiativeRequest.startDate())
                .endDate(initiativeRequest.endDate())
                .build();

        Initiative savedInitiative = initiativeRepository.save(initiative);

        InitiativeMember initiativeMember = new InitiativeMember(savedInitiative, user);
        initiativeMemberRepository.save(initiativeMember);

        notifyUsersInAreaAboutNewInitiative(savedInitiative);

        return savedInitiative;
    }

    private void notifyUsersInAreaAboutNewInitiative(Initiative initiative) {
        List<User> usersInArea = userService.findByLocation(initiative.getLocation());
        List<User> recipients = usersInArea.stream()
                .filter(user -> !user.equals(initiative.getCreator()))
                .toList();

        if (!recipients.isEmpty()) {
            Map<String, Object> notificationData = new HashMap<>();
            notificationData.put("initiative", initiative);
            notificationData.put("actor", initiative.getCreator());

            notificationService.createAndAssignNotification(
                    NotificationType.NEW_INITIATIVE,
                    notificationData,
                    recipients
            );
        }
    }

    public List<Initiative> getAccessibleInitiatives(User user) {
        log.info("Fetching all available initiatives for user: {}", user.getEmail());
        return initiativeRepository.findAllAccessibleByUser(user, user.getLocation());
    }

    public Initiative getInitiativeByIdIfAccessible(Long id, User user) {
        log.info("Fetching initiative by ID: {}", id);
        return initiativeRepository.findAccessibleById(id, user, user.getLocation())
                .orElseThrow(() -> new InitiativeNotFoundException(id));
    }

    public Initiative joinInitiative(Long initiativeId, User user) {
        log.info("User {} is joining initiative with ID: {}", user.getEmail(), initiativeId);
        if (initiativeRepository.isMember(initiativeId, user)) {
            log.warn("User {} is already a member of initiative with ID: {}", user.getEmail(), initiativeId);
            throw new AlreadyInitiativeMemberException("User is already a member of this initiative");
        }
        Initiative initiative = getInitiativeByIdIfAccessible(initiativeId, user);
        InitiativeMember initiativeMember = new InitiativeMember(initiative, user);
        initiativeMemberRepository.save(initiativeMember);
        initiative.getParticipants().add(initiativeMember);

        if (!initiative.getCreator().equals(user)) {
            notificationService.createAndAssignNotification(
                NotificationType.NEW_INITIATIVE,
                Map.of(
                    "title", "New Member Joined",
                    "message", String.format("%s has joined your initiative: %s", user.getName(), initiative.getTitle())
                ),
                initiative.getCreator()
            );
        }

        return initiative;
    }

    public Set<Comment> getCommentsForPost(Long initiativeId, Long postId, User user) {
        log.info("Fetching comments for post ID: {} in initiative ID: {}", postId, initiativeId);
        Post post = postRepository.findAccessibleById(postId, user, user.getLocation())
                .orElseThrow(() -> new PostNotFoundException(postId));
        return post.getComments();
    }
}
