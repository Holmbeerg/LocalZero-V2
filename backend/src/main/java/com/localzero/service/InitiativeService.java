package com.localzero.service;

import com.localzero.exception.AlreadyInitiativeMemberException;
import com.localzero.exception.InitiativeNotFoundException;
import com.localzero.model.*;
import com.localzero.dto.CreateInitiativeRequest;
import com.localzero.model.enums.NotificationType;
import com.localzero.repository.InitiativeMemberRepository;
import com.localzero.repository.InitiativeRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Transactional
@Service
@Slf4j
public class InitiativeService {
    private final InitiativeRepository initiativeRepository;
    private final InitiativeMemberRepository initiativeMemberRepository;
    private final NotificationService notificationService;
    private final UserService userService;

    public InitiativeService(InitiativeRepository initiativeRepository, InitiativeMemberRepository initiativeMemberRepository,
                             NotificationService notificationService, UserService userService) {
        this.initiativeRepository = initiativeRepository;
        this.initiativeMemberRepository = initiativeMemberRepository;
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
        System.out.println("Outside if");

        notificationService.createAndAssignNotification(
                NotificationType.NEW_INITIATIVE,
                recipients,  // List<User>
                initiative.getCreator(),
                Map.of(
                        "initiative", initiative.getTitle(),
                        "createdBy", initiative.getCreator().getName()
                )
        );
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

    @Transactional
    public Initiative joinInitiative(Long initiativeId, User user) {
        Initiative initiative = initiativeRepository.findAccessibleById(initiativeId, user, user.getLocation())
                .orElseThrow(() -> new InitiativeNotFoundException(initiativeId));

        if (initiativeRepository.isMember(initiativeId, user)) {
            log.warn("User {} is already a member of initiative with ID: {}", user.getEmail(), initiativeId);
            throw new AlreadyInitiativeMemberException("User is already a member of this initiative");
        }

        InitiativeMemberId memberId = new InitiativeMemberId(initiativeId, user.getUserId());

        InitiativeMember member = new InitiativeMember();
        member.setId(memberId);
        member.setInitiative(initiative);
        member.setUser(user);

        initiativeMemberRepository.save(member);

        notificationService.createAndAssignNotification(
                NotificationType.JOIN_INITIATIVE,
                initiative.getCreator(),
                user,
                Map.of(
                        "initiative", initiative.getTitle(),
                        "joinedBy", user.getName()
                )
        );
        return initiative;
    }
}