package com.localzero.service;

import com.localzero.dto.MessageRequest;
import com.localzero.exception.CannotSendMessageToSelfException;
import com.localzero.exception.UserNotFoundException;
import com.localzero.mapper.MessageMapper;
import com.localzero.model.*;
import com.localzero.repository.MessagesRepository;
import com.localzero.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
@Slf4j
public class MessagesService {
    private final MessagesRepository messagesRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    public MessagesService(MessagesRepository messagesRepository, UserRepository userRepository, MessageMapper messageMapper) {
        this.messagesRepository = messagesRepository;
        this.userRepository = userRepository;
        this.messageMapper = messageMapper;
    }

    public List<Message> getUserMessages(User user) {
        log.info("Fetching all available messages for user ID {}", user.getEmail());
        return messagesRepository.findAllByReceiver(user);
    }

    public void sendUserMessage(MessageRequest message, String senderEmail) {
        String receiverEmail = message.receiverEmail();

        User sender = userRepository.findByEmail(senderEmail).orElseThrow(() ->
                new UserNotFoundException("Sender user " + senderEmail + " not found"));

        User receiver = userRepository.findByEmail(receiverEmail).orElseThrow(() ->
                new UserNotFoundException("Receiver user " + receiverEmail + " not found"));

        log.info("User {} is sending message to user {}", senderEmail, receiverEmail);
        if (sender.getEmail().equals(receiver.getEmail())) {
            log.warn("User {} tried to send message to self", sender);
            throw new CannotSendMessageToSelfException("User can't send message to self");
        }

        Message newMessage = new Message();
        newMessage.setSender(sender);
        newMessage.setReceiver(receiver);
        newMessage.setText(message.text());

        messagesRepository.save(newMessage);
    }
}
