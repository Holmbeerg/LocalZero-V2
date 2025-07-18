package com.localzero.service;

import com.localzero.exception.CannotSendMessageToSelfException;
import com.localzero.model.*;
import com.localzero.repository.MessagesRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Transactional
@Service
@Slf4j
public class MessagesService {
    private final MessagesRepository messagesRepository;

    public MessagesService(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    public List<Message> getUserMessages(User user) {
        log.info("Fetching all available messages for user: {}", user.getName());
        return messagesRepository.getUserMessages(user.getUserId());
    }

    public Message sendMessage(Message message) {
        log.info("User {} is sending message to user: {}", message.getSender(), message.getReceiver());
        if (message.getSender().equals(message.getReceiver())) {
            log.warn("User {} tried to send message to self", message.getSender());
            throw new CannotSendMessageToSelfException("User can't send message to self");
        }
        messagesRepository.insertMessage(message);

        return message;
    }
}
