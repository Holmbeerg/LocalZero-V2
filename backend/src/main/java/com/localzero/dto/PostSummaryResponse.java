package com.localzero.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PostSummaryResponse(
        Long id,
        Long initiativeId,
        String text,
        List<String> imageUrls,
        UserSummaryResponse author,
        String createdAt,
        int likeCount,
        int commentCount
) { }
