package com.localzero.service;

import com.localzero.exception.EcoActionNotFoundException;
import com.localzero.model.EcoAction;
import com.localzero.model.EcoActionType;
import com.localzero.model.User;
import com.localzero.dto.LogEcoActionRequest;
import com.localzero.repository.EcoActionRepository;
import com.localzero.repository.EcoActionTypeRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
@Slf4j // Using Lombok's @Slf4j for logging
@RequiredArgsConstructor
public class EcoActionService {

    private final EcoActionRepository ecoActionRepository;
    private final EcoActionTypeRepository ecoActionTypeRepository;
    private final UserService userService;


    public EcoAction logEcoAction(LogEcoActionRequest request, String email) {
        log.info("Logging eco action for user: {} with actionId: {}", email, request.actionId());
        User user = userService.getUserByEmail(email);

        EcoActionType ecoActionType = ecoActionTypeRepository.findById(request.actionId())
                .orElseThrow(() -> new EcoActionNotFoundException("EcoActionType with id " + request.actionId() + " not found"));

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
