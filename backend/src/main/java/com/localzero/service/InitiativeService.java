package com.localzero.service;

import com.localzero.model.Initiative;
import com.localzero.model.User;
import com.localzero.model.dto.CreateInitiativeRequest;
import com.localzero.repository.InitiativeRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Transactional
@Service
@Slf4j
public class InitiativeService {
    private final InitiativeRepository initiativeRepository;
    private final UserService userService;

    public InitiativeService(InitiativeRepository initiativeRepository, UserService userService) {
        this.initiativeRepository = initiativeRepository;
        this.userService = userService;
    }

    public Initiative createInitiative(CreateInitiativeRequest initiativeRequest, String email) {
        log.info("Creating initiative for user: {} with title: {}", email, initiativeRequest.title());
        User user = userService.getUserByEmail(email);

        Initiative initiative = Initiative.builder()
                .title(initiativeRequest.title())
                .description(initiativeRequest.description())
                .creator(user)
                .location(initiativeRequest.location())
                .category(initiativeRequest.category())
                .isPublic(initiativeRequest.isPublic())
                .startDate(initiativeRequest.startDate())
                .endDate(initiativeRequest.endDate())
                .build();

        return initiativeRepository.save(initiative);
    }
}
