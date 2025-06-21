package com.localzero.mapper;

import com.localzero.dto.PostSummaryResponse;
import com.localzero.model.Post;
import com.localzero.model.PostImage;
import com.localzero.service.S3Service;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class PostMapper {

    private final UserMapper userMapper;
    private final S3Service s3Service;

    public PostMapper(UserMapper userMapper, S3Service s3Service) {
        this.userMapper = userMapper;
        this.s3Service = s3Service;
    }

    public PostSummaryResponse toPostSummaryResponse(Post post) {
        return PostSummaryResponse.builder()
                .id(post.getId())
                .initiativeId(post.getInitiative().getId())
                .text(post.getText())
                .imageUrls(getImageUrls(post.getImages()))
                .author(userMapper.toUserSummaryResponse(post.getAuthor()))
                .createdAt(post.getCreatedAt().toString())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .build();
    }

    private List<String> getImageUrls(Set<PostImage> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }

        Duration urlExpiration = Duration.ofMinutes(15);

        return images.stream()
                .map(image -> s3Service.generatePresignedDownload(image.getS3Key(), urlExpiration))
                .toList();
    }
}
