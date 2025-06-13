package com.localzero.service;

import com.localzero.model.Initiative;
import com.localzero.model.InitiativeMember;
import com.localzero.model.User;
import com.localzero.dto.CreateInitiativeRequest;
import com.localzero.repository.InitiativeMemberRepository;
import com.localzero.repository.InitiativeRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
@Slf4j
public class InitiativeService {
    private final InitiativeRepository initiativeRepository;
    private final UserService userService;
    private final InitiativeMemberRepository initiativeMemberRepository;

    public InitiativeService(InitiativeRepository initiativeRepository, UserService userService, InitiativeMemberRepository initiativeMemberRepository) {
        this.initiativeRepository = initiativeRepository;
        this.userService = userService;
        this.initiativeMemberRepository = initiativeMemberRepository;
    }

    public Initiative createInitiative(CreateInitiativeRequest initiativeRequest, String email) {
        log.info("Creating initiative for user: {} with title: {}", email, initiativeRequest.title());
        User user = userService.getUserByEmail(email);

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

        return savedInitiative;
    }

    public List<Initiative> getAccessibleInitiatives(String email) {
        log.info("Fetching all available initiatives for user: {}", email);
        User user = userService.getUserByEmail(email);
        log.info("fetching initiatives for user: {} in location: {}", user.getEmail(), user.getLocation());
        return initiativeRepository.findAllAccessibleByUser(user, user.getLocation());
    }
}
