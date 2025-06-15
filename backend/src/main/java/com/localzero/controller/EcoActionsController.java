package com.localzero.controller;

import com.localzero.mapper.EcoActionMapper;
import com.localzero.model.EcoAction;
import com.localzero.dto.EcoActionResponse;
import com.localzero.dto.LogEcoActionRequest;
import com.localzero.service.EcoActionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eco-actions")
@Slf4j
public class EcoActionsController {

    private final EcoActionService ecoActionService;
    private final EcoActionMapper ecoActionMapper;

    public EcoActionsController(EcoActionService ecoActionService, EcoActionMapper ecoActionMapper) {
        this.ecoActionService = ecoActionService;
        this.ecoActionMapper = ecoActionMapper;
    }


    @PostMapping
    public ResponseEntity<EcoActionResponse> logEcoAction(@Valid @RequestBody LogEcoActionRequest logEcoActionRequest, @AuthenticationPrincipal UserDetails userDetails) {
        EcoAction savedAction = ecoActionService.logEcoAction(logEcoActionRequest, userDetails.getUsername());
        log.info("Eco action logged for user: {} with actionId: {}", userDetails.getUsername(), logEcoActionRequest.actionId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ecoActionMapper.toResponse(savedAction));
    }

    @GetMapping
    public ResponseEntity<List<EcoActionResponse>> getUserEcoActions(@AuthenticationPrincipal UserDetails userDetails) {
        List<EcoAction> ecoActions = ecoActionService.getUserEcoActionsSortedByDate(userDetails.getUsername());
        List<EcoActionResponse> response = ecoActions.stream()
                .map(ecoActionMapper::toResponse)
                .toList();
        log.info("Retrieved eco actions for user: {}", userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
