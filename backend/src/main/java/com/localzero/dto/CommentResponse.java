package com.localzero.dto;

import lombok.Builder;

@Builder
public record CommentResponse(
        Long id,
        Long postId,
        String text,
        UserSummaryResponse author,
        String createdAt
) { }
