package com.localzero.service;

import com.localzero.dto.CreatePostRequest;
import com.localzero.exception.EcoActionNotFoundException;
import com.localzero.model.EcoAction;
import com.localzero.model.EcoActionType;
import com.localzero.model.Initiative;
import com.localzero.model.User;
import com.localzero.dto.LogEcoActionRequest;
import com.localzero.model.enums.NotificationType;
import com.localzero.repository.EcoActionRepository;
import com.localzero.repository.EcoActionTypeRepository;
import com.localzero.repository.InitiativeMemberRepository;
import com.localzero.repository.InitiativeRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Transactional
@Service
@Slf4j // Using Lombok's @Slf4j for logging
public class EcoActionService {

    private final EcoActionRepository ecoActionRepository;
    private final EcoActionTypeRepository ecoActionTypeRepository;
    private final UserService userService;

    private final InitiativeMemberRepository initiativeMemberRepository;
    private final InitiativeRepository initiativeRepository;
    private final PostService postService;
    private final NotificationService notificationService;

    public EcoActionService(EcoActionRepository ecoActionRepository,
                            EcoActionTypeRepository ecoActionTypeRepository,
                            UserService userService,
                            InitiativeMemberRepository initiativeMemberRepository,    // <— AGGIUNTO
                            InitiativeRepository initiativeRepository,              // <— AGGIUNTO
                            PostService postService,                                // <— AGGIUNTO
                            NotificationService notificationService) {              // <— AGGIUNTO
        this.ecoActionRepository = ecoActionRepository;
        this.ecoActionTypeRepository = ecoActionTypeRepository;
        this.userService = userService;
        this.initiativeMemberRepository = initiativeMemberRepository;              // <— AGGIUNTO
        this.initiativeRepository = initiativeRepository;                          // <— AGGIUNTO
        this.postService = postService;                                            // <— AGGIUNTO
        this.notificationService = notificationService;                            // <— AGGIUNTO
    }

    public EcoAction logEcoAction(LogEcoActionRequest request, String email) {
        User user = userService.getUserByEmail(email);
        EcoActionType type = ecoActionTypeRepository.findById(request.actionId())
                .orElseThrow(() -> new EcoActionNotFoundException("EcoActionType not found: " + request.actionId()));

        EcoAction ecoAction = new EcoAction();
        ecoAction.setUser(user);
        ecoAction.setEcoActionType(type);
        ecoAction.setActionDate(request.date());
        ecoAction = ecoActionRepository.save(ecoAction);

        try {
            var initiativeIds = initiativeMemberRepository.findInitiativeIdsByUserId(user.getUserId());
            for (Long iid : initiativeIds) {
                String author = user.getName() != null ? user.getName() : user.getEmail();
                String text = author + " logged \"" + type.getAction() + "\" (" +
                        type.getCarbonSaved().stripTrailingZeros().toPlainString() + " kg CO₂) on " + request.date();
                postService.createPost(iid, new CreatePostRequest(iid, text, java.util.List.of()), email);

                var initiative = initiativeRepository.findById(iid).orElse(null);
                if (initiative == null) continue;

                var recipients = initiativeMemberRepository.findUsersByInitiativeId(iid).stream()
                        .filter(u -> !u.getUserId().equals(user.getUserId())) // escludi autore
                        .toList();

                if (!recipients.isEmpty()) {
                    var data = java.util.Map.<String,Object>of(
                            "initiative", initiative,
                            "performedBy", user,
                            "actionName",  type.getAction(),
                            "carbonKg",    type.getCarbonSaved(),
                            "date",        request.date()
                    );
                    notificationService.createAndAssignNotification(NotificationType.NEW_ECO_ACTION, data, recipients);
                }
            }
        } catch (Exception e) {
            log.warn("EcoAction propagation failed for user {}", email, e);
        }

        return ecoAction;
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
