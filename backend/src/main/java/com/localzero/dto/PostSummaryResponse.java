package com.localzero.dto;

import java.util.List;

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
