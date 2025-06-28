package com.localzero.mapper;

import com.localzero.dto.CommentResponse;
import com.localzero.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private final UserMapper userMapper;

    public CommentMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public CommentResponse toResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .text(comment.getText())
                .author(userMapper.toUserSummaryResponse(comment.getAuthor()))
                .createdAt(comment.getCreatedAt().toString())
                .build();
    }
}
