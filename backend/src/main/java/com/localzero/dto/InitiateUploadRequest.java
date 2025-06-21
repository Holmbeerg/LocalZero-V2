package com.localzero.dto;

import jakarta.validation.constraints.NotBlank;


public record InitiateUploadRequest(
        @NotBlank String fileName,
        @NotBlank String contentType
) { }