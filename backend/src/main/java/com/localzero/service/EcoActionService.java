package com.localzero.service;

import com.localzero.model.EcoAction;
import com.localzero.model.EcoActionType;
import com.localzero.model.User;
import com.localzero.model.dto.LogEcoActionRequest;
import com.localzero.repository.EcoActionRepository;
import com.localzero.repository.EcoActionTypeRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
@Slf4j // Using Lombok's @Slf4j for logging
public class EcoActionService {

    private final EcoActionRepository ecoActionRepository;
    private final EcoActionTypeRepository ecoActionTypeRepository;
    private final UserService userService;

    public EcoActionService(EcoActionRepository ecoActionRepository,
                            EcoActionTypeRepository ecoActionTypeRepository, UserService userService) {
        this.ecoActionRepository = ecoActionRepository;
        this.ecoActionTypeRepository = ecoActionTypeRepository;
        this.userService = userService;
    }

    public EcoAction logEcoAction(LogEcoActionRequest request, String email) {
        log.info("Logging eco action for user: {} with actionId: {}", email, request.actionId());
        User user = userService.getUserByEmail(email);

        EcoActionType ecoActionType = ecoActionTypeRepository.findById(request.actionId())
                .orElseThrow(() -> new RuntimeException("EcoActionType not found: " + request.actionId()));

        EcoAction ecoAction = EcoAction.builder()
                .user(user)
                .ecoActionType(ecoActionType)
                .actionDate(request.date())
                .build();

        return ecoActionRepository.save(ecoAction);
    }

    public List<EcoAction> getUserEcoActions(String email) {
        User user = userService.getUserByEmail(email);
        return ecoActionRepository.findByUser(user);
    }

    public List<EcoAction> getUserEcoActionsSortedByDate(String email) {
        User user = userService.getUserByEmail(email);
        return ecoActionRepository.findByUserOrderByActionDateDesc(user);
    }
}
