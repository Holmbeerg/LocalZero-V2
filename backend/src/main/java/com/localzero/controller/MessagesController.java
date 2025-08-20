package com.localzero.controller;

import com.localzero.dto.*;
import com.localzero.exception.CannotSendMessageToSelfException;
import com.localzero.exception.UserNotFoundException;
import com.localzero.mapper.MessageMapper;
import com.localzero.model.*;
import com.localzero.service.MessagesService;
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

    // for now automatically gets all messages received by current user
    @GetMapping
    public ResponseEntity<List<MessageResponse>> getAllMessages(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByEmail(userDetails.getUsername());
        List<Message> messages = messagesService.getUserMessages(user);
        log.info("Retrieved messages for user: {}", userDetails.getUsername());
        List<MessageResponse> responses = messages.stream()
                .map(messageMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<String> postMessage(@Valid @RequestBody MessageRequest messageRequest,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        String senderEmail = userDetails.getUsername();
        String receiverEmail = messageRequest.receiverEmail();
        String text = messageRequest.text();

        if (senderEmail == null || senderEmail.isBlank()) {
            log.info("Message failed to send to receiver user {}, invalid sender email", receiverEmail);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sender email is required");
        }

        if (receiverEmail == null || receiverEmail.isBlank()) {
            log.info("Message failed to send to receiver user {}, invalid receiver email", receiverEmail);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Receiver email is required");
        }

        if (text == null || text.isBlank()) {
            log.info("Message failed to send to receiver user {}, missing text content", receiverEmail);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Text content is required");
        }

        try {
            messagesService.sendUserMessage(messageRequest, senderEmail);
            log.info("Message sent to receiver user {}", receiverEmail);
            return ResponseEntity.
                    status(HttpStatus.CREATED).
                    body("Message sent successfully");
        } catch (UserNotFoundException | CannotSendMessageToSelfException se) {
            log.info("Message failed to send to receiver user {}, invalid request", receiverEmail);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(se.getMessage());
        } catch (Exception e) {
            log.info("Message failed to send to receiver user {}", receiverEmail);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Message failed to send");
        }
    }
}

