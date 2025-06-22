package com.localzero.service;

import com.localzero.dto.CreatePostRequest;
import com.localzero.exception.InitiativeNotFoundException;
import com.localzero.model.Initiative;
import com.localzero.model.Post;
import com.localzero.model.PostImage;
import com.localzero.model.User;
import com.localzero.repository.InitiativeRepository;
import com.localzero.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class PostService {

    private final InitiativeRepository initiativeRepository;
    private final UserService userService;
    private final S3Service s3Service;
    private final PostRepository postRepository;

    public PostService(InitiativeRepository initiativeRepository, UserService userService, S3Service s3Service, PostRepository postRepository) {
        this.initiativeRepository = initiativeRepository;
        this.userService = userService;
        this.s3Service = s3Service;
        this.postRepository = postRepository;
    }

    public Post createPost(Long initiativeId, CreatePostRequest createPostRequest, String email) {
        Initiative initiative = initiativeRepository.findById(initiativeId)
                .orElseThrow(() -> new InitiativeNotFoundException(initiativeId));

        User user = userService.getUserByEmail(email);

        Post post = Post.builder()
                .initiative(initiative)
                .author(user)
                .text(createPostRequest.text())
                .build();

        if (createPostRequest.imageKeys() != null && !createPostRequest.imageKeys().isEmpty()) {
            for (String s3Key : createPostRequest.imageKeys()) {
                s3Service.confirmUpload(s3Key);

                PostImage postImage = PostImage.builder()
                        .s3Key(s3Key)
                        .build();

                post.addImage(postImage);
            }
        }
        return postRepository.save(post);
    }
}
