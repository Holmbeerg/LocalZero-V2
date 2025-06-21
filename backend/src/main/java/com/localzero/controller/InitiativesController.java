package com.localzero.controller;

import com.localzero.dto.*;
import com.localzero.mapper.InitiativeMapper;
import com.localzero.mapper.PostMapper;
import com.localzero.model.Initiative;
import com.localzero.model.Post;
import com.localzero.model.User;
import com.localzero.service.InitiativeService;
import com.localzero.service.PostService;
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
    private final PostService postService;
    private final PostMapper postMapper;


    public InitiativesController(InitiativeService initiativeService, InitiativeMapper initiativeMapper, UserService userService, PostService postService, PostMapper postMapper) {
        this.initiativeService = initiativeService;
        this.initiativeMapper = initiativeMapper;
        this.userService = userService;
        this.postService = postService;
        this.postMapper = postMapper;
    }

    @PostMapping
    public ResponseEntity<InitiativeSummaryResponse> createInitiative(@Valid @RequestBody CreateInitiativeRequest initiativeRequest,
                                                                      @AuthenticationPrincipal
                                                                   UserDetails userDetails) {

        User user = userService.getUserByEmail(userDetails.getUsername());
        Initiative initiative = initiativeService.createInitiative(initiativeRequest, user);

        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(initiativeMapper.toResponse(initiative, user));
    }

    @GetMapping
    public ResponseEntity<List<InitiativeSummaryResponse>> getAllInitiatives(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        List<Initiative> initiatives = initiativeService.getAccessibleInitiatives(user);
        log.info("Retrieved initiatives for user: {}", userDetails.getUsername());
        List<InitiativeSummaryResponse> responses = initiatives.stream()
                .map(initiative -> initiativeMapper.toResponse(initiative, user))
                .toList();

        return ResponseEntity.ok(responses);
    }


    @GetMapping("/{id}")
    public ResponseEntity<InitiativeDetailResponse> getInitiativeById(@PathVariable Long id,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        Initiative initiative = initiativeService.getInitiativeByIdIfAccessible(id, user);

        InitiativeDetailResponse response = initiativeMapper.toDetailResponse(initiative, user);
        log.info("Retrieved initiative by ID: {} for user: {}", id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<InitiativeSummaryResponse> joinInitiative(@PathVariable Long id,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.getUserByEmail(userDetails.getUsername());
        Initiative initiative = initiativeService.joinInitiative(id, user);
        log.info("User: {} joined initiative with ID: {}", userDetails.getUsername(), id);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(initiativeMapper.toResponse(initiative, user));
    }

    @PostMapping("/{id}/posts")
    public ResponseEntity<PostSummaryResponse> createPostInInitiative(@PathVariable Long id,
                                                                      @Valid @RequestBody CreatePostRequest createPostRequest,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.createPost(id, createPostRequest, userDetails.getUsername());
        log.info("User: {} created a post in initiative with ID: {}", userDetails.getUsername(), id);
        return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.toPostSummaryResponse(post));
    }
}

