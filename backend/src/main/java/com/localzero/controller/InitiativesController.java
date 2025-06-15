package com.localzero.controller;

import com.localzero.dto.InitiativeDetailResponse;
import com.localzero.mapper.InitiativeMapper;
import com.localzero.model.Initiative;
import com.localzero.dto.CreateInitiativeRequest;
import com.localzero.dto.InitiativeListResponse;
import com.localzero.model.User;
import com.localzero.service.InitiativeService;
import com.localzero.service.UserService;
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
    private final UserService userService;


    public InitiativesController(InitiativeService initiativeService, InitiativeMapper initiativeMapper, UserService userService) {
        this.initiativeService = initiativeService;
        this.initiativeMapper = initiativeMapper;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<InitiativeListResponse> createInitiative(@Valid @RequestBody CreateInitiativeRequest initiativeRequest,
                                                                   @AuthenticationPrincipal
                                                                   UserDetails userDetails) {

        User user = userService.getUserByEmail(userDetails.getUsername());
        Initiative initiative = initiativeService.createInitiative(initiativeRequest, user);

        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(initiativeMapper.toResponse(initiative, user));
    }

    @GetMapping
    public ResponseEntity<List<InitiativeListResponse>> getAllInitiatives(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        List<Initiative> initiatives = initiativeService.getAccessibleInitiatives(user);
        log.info("Retrieved initiatives for user: {}", userDetails.getUsername());
        List<InitiativeListResponse> responses = initiatives.stream()
                .map(initiative -> initiativeMapper.toResponse(initiative, user))
                .toList();

        return ResponseEntity.ok(responses);
    }


    @GetMapping("/{id}")
    public ResponseEntity<InitiativeDetailResponse> getInitiativeById(@PathVariable Long id,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        Initiative initiative = initiativeService.getInitiativeById(id, user);

        InitiativeDetailResponse response = initiativeMapper.toDetailResponse(initiative, user);
        log.info("Retrieved initiative by ID: {} for user: {}", id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}

