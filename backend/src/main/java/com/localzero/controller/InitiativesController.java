package com.localzero.controller;

import com.localzero.mapper.InitiativeMapper;
import com.localzero.model.Initiative;
import com.localzero.dto.CreateInitiativeRequest;
import com.localzero.dto.InitiativeResponse;
import com.localzero.service.InitiativeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/initiatives")
@Slf4j
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

        Initiative initiative = initiativeService.createInitiative(initiativeRequest, userDetails.getUsername());
        InitiativeResponse response = initiativeMapper.toResponse(initiative);

        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(response);
    }

    @GetMapping
    public ResponseEntity<List<InitiativeResponse>> getAllInitiatives(@AuthenticationPrincipal UserDetails userDetails) {
        List<Initiative> initiatives = initiativeService.getAccessibleInitiatives(userDetails.getUsername());
        log.info("Retrieved initiatives for user: {}", userDetails.getUsername());
        List<InitiativeResponse> responses = initiatives.stream()
                .map(initiativeMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    /*
    @GetMapping("/{id}")
    public ResponseEntity<InitiativeResponse> getInitiativeById(@PathVariable Long id,
                                                                @AuthenticationPrincipal UserDetails userDetails) {

        try {
            Initiative initiative = initiativeService.getInitiativeById(id, userDetails.getUsername());
            InitiativeResponse response = initiativeMapper.toResponse(initiative);
            return ResponseEntity.ok(response);
        } catch (InitiativeNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    } */
}

