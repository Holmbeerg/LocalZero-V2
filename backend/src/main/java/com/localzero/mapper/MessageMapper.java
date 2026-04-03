package com.localzero.mapper;

import com.localzero.dto.MessageResponse;
import com.localzero.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final UserMapper userMapper;


    public MessageResponse toResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .sender(userMapper.toUserMessageSummaryResponse(message.getSender()))
                .receiver(userMapper.toUserMessageSummaryResponse(message.getReceiver()))
                .text(message.getText())
                .createdAt(message.getCreatedAt())
                .build();
    }
}