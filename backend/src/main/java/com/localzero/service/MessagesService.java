package com.localzero.service;

import com.localzero.dto.CreateInitiativeRequest;
import com.localzero.exception.AlreadyInitiativeMemberException;
import com.localzero.exception.InitiativeNotFoundException;
import com.localzero.exception.PostNotFoundException;
import com.localzero.model.*;
import com.localzero.repository.InitiativeMemberRepository;
import com.localzero.repository.InitiativeRepository;
import com.localzero.repository.PostRepository;
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

    public MessagesService(MessageRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    public List<Message> getUserMessages(User user) {
        log.info("Fetching all available messages for user: {}", user.getName());
        return messagesRepository.getUserMessages(user.getUserId());
    }

    public Initiative sendMessage(Long initiativeId, User user) {
        log.info("User {} is joining initiative with ID: {}", user.getEmail(), initiativeId);
        if (initiativeRepository.isMember(initiativeId, user)) {
            log.warn("User {} is already a member of initiative with ID: {}", user.getEmail(), initiativeId);
            throw new AlreadyInitiativeMemberException("User is already a member of this initiative");
        }
        Initiative initiative = getInitiativeByIdIfAccessible(initiativeId, user);
        InitiativeMember initiativeMember = new InitiativeMember(initiative, user);
        initiativeMemberRepository.save(initiativeMember);
        initiative.getParticipants().add(initiativeMember);

        return initiative;
    }
}
