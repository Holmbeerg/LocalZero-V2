package com.localzero.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

@Component
@Slf4j
public class S3BucketInitializer implements CommandLineRunner {

    private final S3Client s3Client;
    private final String bucketName;

    // Spring injects the S3Client and the "bucketName" bean from S3Config
    public S3BucketInitializer(S3Client s3Client, @Qualifier("bucketName") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public void run(String... args) {
        log.info("Checking for S3 bucket: {}", bucketName);
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
            log.info("S3 bucket '{}' already exists.", bucketName);
        } catch (NoSuchBucketException e) {
            log.warn("S3 bucket '{}' not found. Creating it now...", bucketName);
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(createBucketRequest);
            log.info("S3 bucket '{}' created successfully.", bucketName);
        }
    }
}