package com.localzero.mapper;

import com.localzero.dto.PostSummaryResponse;
import com.localzero.model.Post;
import com.localzero.model.PostImage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class PostMapper {

    private final UserMapper userMapper;

    public PostMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
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
        return images.stream()
                .map(PostImage::getImageUrl)
                .toList();
    }
}
