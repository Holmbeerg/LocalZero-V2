package com.localzero.controller;

import com.localzero.dto.InitiateUploadRequest;
import com.localzero.dto.PresignedUploadResponse;
import com.localzero.service.S3Service;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/uploads")
@Slf4j
public class UploadController {

    private final S3Service s3Service;

    public UploadController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/initiate")
    public ResponseEntity<PresignedUploadResponse> initiateUpload(
            @Valid @RequestBody InitiateUploadRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("User '{}' initiating upload for file: {}", userDetails.getUsername(), request.fileName());

        PresignedUploadResponse response = s3Service.generatePresignedUpload(
                request.fileName(),
                request.contentType()
        );

        log.info("Generated presigned PUT URL for user '{}': {}", userDetails.getUsername(), response.presignedUrl());
        return ResponseEntity.ok(response);
    }
}
