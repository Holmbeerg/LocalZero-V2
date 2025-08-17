package com.localzero.controller;

import com.localzero.dto.*;
import com.localzero.mapper.CommentMapper;
import com.localzero.mapper.InitiativeMapper;
import com.localzero.mapper.PostMapper;
import com.localzero.model.Comment;
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
import java.util.Set;

@RestController
@RequestMapping("/api/initiatives")
@Slf4j
public class InitiativesController {

    private final InitiativeService initiativeService;
    private final InitiativeMapper initiativeMapper;
    private final UserService userService;
    private final PostService postService;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;


    public InitiativesController(InitiativeService initiativeService, InitiativeMapper initiativeMapper,
                                 UserService userService, PostService postService, PostMapper postMapper
                                , CommentMapper commentMapper) {
        this.initiativeService = initiativeService;
        this.initiativeMapper = initiativeMapper;
        this.userService = userService;
        this.postService = postService;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
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
        User user = userService.getUserByEmail(userDetails.getUsername());
        log.info("User: {} created a post in initiative with ID: {}", userDetails.getUsername(), id);
        return ResponseEntity.status(HttpStatus.CREATED).body(postMapper.toPostSummaryResponse(post, user));
    }

    @PostMapping("/{id}/posts/{postId}/like")
    public ResponseEntity<PostSummaryResponse> likePostInInitiative(@PathVariable Long id,
                                                                    @PathVariable Long postId,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        Post post = postService.toggleLike(id, postId, user);
        return ResponseEntity.ok(postMapper.toPostSummaryResponse(post, user));
    }

    @GetMapping("/{id}/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsForPost(@PathVariable Long id,
                                                                    @PathVariable Long postId,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        Set<Comment> comments = initiativeService.getCommentsForPost(id, postId, user);
        log.info("Retrieved comments for post ID: {} in initiative ID: {} for user: {}", postId, id, userDetails.getUsername());
        List<CommentResponse> commentResponse = comments.stream()
                .map(commentMapper::toResponse)
                .toList();

        return ResponseEntity.ok(commentResponse);
    }

    @PostMapping("/{id}/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> createCommentForPost(@PathVariable Long id,
                                                                @PathVariable Long postId,
                                                                @Valid @RequestBody CreateCommentRequest createCommentRequest,
                                                                @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        Comment comment = initiativeService.createCommentForPost(id, postId, createCommentRequest.text(), user);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.toResponse(comment));
    }
}

