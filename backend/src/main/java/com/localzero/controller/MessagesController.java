package com.localzero.controller;

import com.localzero.dto.*;
import com.localzero.mapper.CommentMapper;
import com.localzero.mapper.InitiativeMapper;
import com.localzero.mapper.MessageMapper;
import com.localzero.mapper.PostMapper;
import com.localzero.model.*;
import com.localzero.service.InitiativeService;
import com.localzero.service.MessagesService;
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
@RequestMapping("/api/messages")
@Slf4j
public class MessagesController {

    private final MessagesService messagesService;
    private final MessageMapper messageMapper;
    private final UserService userService;


    public MessagesController(MessagesService messagesService, MessageMapper messageMapper,
                                 UserService userService) {
        this.messagesService = messagesService;
        this.messageMapper = messageMapper;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<MessageResponse>> getAllMessages(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        List<Message> messages = messagesService.getUserMessages(user);
        log.info("Retrieved messages for user: {}", userDetails.getUsername());
        List<MessageResponse> responses = messages.stream()
                .map(message -> messageMapper.toResponse(message))
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<UserSummaryResponse> getUserById(@PathVariable Long id,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {
        UserSummaryResponse userSummary = userService.getUserSummaryById(id);

        log.info("Retrieved user {} for user {} by ID: {}", userSummary.name(), userDetails.getUsername(), id);
        return ResponseEntity.ok(userSummary);
    }

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@Valid @RequestBody MessageResponse messageResponse,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.getUserByEmail(userDetails.getUsername());
        Initiative initiative = initiativeService.joinInitiative(id, user);
        log.info("User: {} sent message to user with ID: {}", userDetails.getUsername(), id);
        // TODO: what should this return?
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(initiativeMapper.toResponse(initiative, user));
    }
}

