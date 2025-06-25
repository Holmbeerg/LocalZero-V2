package com.localzero.service;

import com.localzero.dto.CreatePostRequest;
import com.localzero.exception.InitiativeNotFoundException;
import com.localzero.model.*;
import com.localzero.repository.InitiativeRepository;
import com.localzero.repository.LikeRepository;
import com.localzero.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
@Slf4j
public class PostService {

    private final InitiativeRepository initiativeRepository;
    private final UserService userService;
    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final EntityManager entityManager;

    public PostService(InitiativeRepository initiativeRepository, UserService userService, S3Service s3Service,
                       PostRepository postRepository, LikeRepository likeRepository, EntityManager entityManager) {
        this.initiativeRepository = initiativeRepository;
        this.userService = userService;
        this.s3Service = s3Service;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.entityManager = entityManager;
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

    public Post toggleLike(Long initiativeId, Long postId, User user) {
        initiativeRepository.findById(initiativeId)
                .orElseThrow(() -> new InitiativeNotFoundException(initiativeId));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        Optional<Like> existingLikeOpt = likeRepository.findByPostAndUser(post, user);

        if (existingLikeOpt.isPresent()) {
            Like likeToRemove = existingLikeOpt.get();
            post.removeLike(likeToRemove);
            log.info("User {} removed like from post {}", user.getEmail(), postId);

        } else {
            Like newLike = Like.builder()
                    .post(post)
                    .user(user)
                    .build();

            post.addLike(newLike);
            log.info("User {} liked post {}", user.getEmail(), postId);
        }
        postRepository.flush();
        entityManager.refresh(post);
        return post;
    }
}
