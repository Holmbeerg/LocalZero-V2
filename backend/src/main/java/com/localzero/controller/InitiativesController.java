package com.localzero.controller;

import com.localzero.mapper.InitiativeMapper;
import com.localzero.model.Initiative;
import com.localzero.model.dto.CreateInitiativeRequest;
import com.localzero.model.dto.InitiativeResponse;
import com.localzero.service.InitiativeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/initiatives")
public class InitiativesController {

    private final InitiativeService initiativeService;
    private final InitiativeMapper initiativeMapper;

    public InitiativesController(InitiativeService initiativeService, InitiativeMapper initiativeMapper) {
        this.initiativeService = initiativeService;
        this.initiativeMapper = initiativeMapper;
    }

    @PostMapping
    public ResponseEntity<InitiativeResponse> createInitiative(@Valid @RequestBody CreateInitiativeRequest initiativeRequest,
                                                               @AuthenticationPrincipal
                                                               UserDetails userDetails) {
        String email = userDetails.getUsername();
        Initiative initiative = initiativeService.createInitiative(initiativeRequest, email);
        InitiativeResponse response = initiativeMapper.toResponse(initiative);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
