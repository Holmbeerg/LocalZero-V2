package com.localzero.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record PresignedUploadResponse(
     String uploadUrl,
     String key,
     Instant expiresAt
) { }
