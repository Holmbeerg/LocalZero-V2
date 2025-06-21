package com.localzero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public record CreatePostRequest(
        @NotNull long initiativeId,
        @NotBlank String text,
        List<String> imageKeys
) { }
