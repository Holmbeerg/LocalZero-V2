package com.localzero.dto;

public record CommentResponse(
        Long id,
        Long postId,
        String text,
        UserSummaryResponse author,
        String createdAt
) { }
