package com.localzero.mapper;

import com.localzero.dto.MessageResponse;
import com.localzero.model.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    private final UserMapper userMapper;

    public MessageMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public MessageResponse toResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .sender(userMapper.toUserSummaryResponse(message.getSender()))
                .receiver(userMapper.toUserSummaryResponse(message.getReceiver()))
                .text(message.getText())
                .createdAt(message.getCreatedAt())
                .build();
    }
}