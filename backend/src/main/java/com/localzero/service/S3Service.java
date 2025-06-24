package com.localzero.service;

import com.localzero.dto.PresignedUploadResponse;
import com.localzero.exception.FailureGeneratingPresignedURLException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class S3Service {

    private final S3Presigner presigner;
    private final String bucketName;
    private final S3Client s3Client;
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp"
    );
    private static final long MAX_FILE_SIZE_MB = (long) 5 * 1024 * 1024; // 5 MB

    public S3Service(S3Client s3Client, @Qualifier("bucketName") String bucketName, S3Presigner presigner) {
        this.bucketName = bucketName;
        this.s3Client = s3Client;
        this.presigner = presigner;
    }

    public PresignedUploadResponse generatePresignedUpload(String fileName, String contentType, long contentLength) {
        validateFileSize(contentLength);
        String uniqueKey = generateUniqueKey(fileName);
        try {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueKey)
                    .contentType(contentType)
                    .contentLength(contentLength)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(15))
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

            return PresignedUploadResponse.builder()
                    .presignedUrl(presignedRequest.url().toString())
                    .key(uniqueKey)
                    .expiresAt(Instant.now().plus(Duration.ofMinutes(15)))
                    .build();

        } catch (Exception e) {
            log.error("Error generating presigned upload URL", e);
            throw new FailureGeneratingPresignedURLException("Failed to generate presigned upload URL", e);
        }
    }


    public String generatePresignedDownload(String key, Duration expiration) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(expiration)
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            return presignedRequest.url().toExternalForm();
        } catch (Exception e) {
            log.error("Failed to generate presigned URL for: {}", key, e);
            throw new RuntimeException("Could not generate presigned URL", e);
        }
    }

    public void confirmUpload(String key) { // TODO: improved validation, quarantine bucket before uploading to actual?
        try {
            // Check if object exists in S3
            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            HeadObjectResponse headResponse = s3Client.headObject(headRequest); // Throws exception if not found

            validateFileSize(headResponse.contentLength());
            validateContentType(key);

        } catch (NoSuchKeyException e) {
            log.error("Object with key '{}' does not exist in bucket '{}'", key, bucketName, e);
            throw new RuntimeException("File upload confirmation failed: Object not found", e);
        } catch (Exception e) {
            log.error("Error confirming upload for key: {}", key, e);
            throw new RuntimeException("File upload confirmation failed", e);
        }
    }

    private void validateContentType(String s3Key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest)) {
            Tika tika = new Tika();
            String detectedType = tika.detect(s3Object);

            if (!ALLOWED_CONTENT_TYPES.contains(detectedType)) {
                log.error("Invalid file content type: {}", detectedType);
                deleteObject(s3Key);
                throw new IllegalArgumentException("Unsupported file content type: " + detectedType);
            }
        } catch (Exception e) {
            log.error("Error validating file content type for: {}", s3Key, e);
            deleteObject(s3Key);
            throw new RuntimeException("Failed to validate file content type", e);
        }
    }


    private void validateFileSize(long fileSize) {
        if (fileSize > MAX_FILE_SIZE_MB) {
            log.error("File size exceeds limit: {} bytes", fileSize);
            throw new IllegalArgumentException("File size exceeds limit: " + fileSize + " bytes");
        }
    }

    private String generateUniqueKey(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        return "images/" + UUID.randomUUID() + extension;
    }

    private void deleteObject(String key) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());
            log.info("Deleted invalid object with key: {}", key);
        } catch (Exception e) {
            log.error("Failed to delete invalid object with key: {}", key, e);
        }
    }
}
