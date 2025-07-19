package com.localzero.dto;

import lombok.Builder;

@Builder
public record MessageRequest(
        String receiverEmail,
        String text
) { }
