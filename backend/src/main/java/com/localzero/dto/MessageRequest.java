package com.localzero.dto;

import lombok.Builder;

@Builder
public record MessageRequest(
        Long senderId,
        Long receiverId,
        String text
) { }
