package com.localzero.service;

import com.localzero.dto.CreatePostRequest;
import com.localzero.exception.InitiativeNotFoundException;
import com.localzero.exception.PostNotFoundException;
import com.localzero.model.*;
import com.localzero.model.enums.NotificationType;
import com.localzero.repository.CommentRepository;
import com.localzero.repository.InitiativeRepository;
import com.localzero.repository.LikeRepository;
import com.localzero.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
@Slf4j
public class PostService {

    private final InitiativeRepository initiativeRepository;
    private final UserService userService;
    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final EntityManager entityManager;
    private final NotificationService notificationService;

    public PostService(InitiativeRepository initiativeRepository, UserService userService, S3Service s3Service,
                       PostRepository postRepository, LikeRepository likeRepository, EntityManager entityManager,
                       NotificationService notificationService, CommentRepository commentRepository) {
        this.initiativeRepository = initiativeRepository;
        this.userService = userService;
        this.s3Service = s3Service;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.entityManager = entityManager;
        this.notificationService = notificationService;
        this.commentRepository = commentRepository;
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
                .orElseThrow(() -> new PostNotFoundException(postId));

        if (!post.getInitiative().getId().equals(initiativeId)) {
            throw new PostNotFoundException(postId);
        }

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

            if (!post.getAuthor().equals(user)) {
                notificationService.createAndAssignNotification(
                        NotificationType.NEW_LIKE_ON_POST,
                        Map.of(
                                "post", post,
                                "likedBy", user
                        ),
                        post.getAuthor()
                );
            }
        }
        postRepository.flush();
        entityManager.refresh(post);
        return post;
    }

    public Set<Comment> getCommentsForPost(Long initiativeId, Long postId, User user) {
        log.info("Fetching comments for post ID: {} in initiative ID: {}", postId, initiativeId);
        Post post = postRepository.findAccessibleById(postId, user, user.getLocation())
                .orElseThrow(() -> new PostNotFoundException(postId));
        return post.getComments();
    }

    public Comment createCommentForPost(Long initiativeId, Long postId, String text, User user){
        log.info("Creating comment for post ID: {} in initiative ID: {} by user: {}", postId, initiativeId, user.getEmail());
        Post post = postRepository.findAccessibleById(postId, user, user.getLocation())
                .orElseThrow(()-> new PostNotFoundException(postId));
        Comment comment = Comment.builder().post(post).author(user).text(text).build();

        if (!post.getAuthor().equals(user)) {
            notificationService.createAndAssignNotification(
                    NotificationType.POST_COMMENT,
                    Map.of(
                            "post", post,
                            "comment", comment,
                            "commentedBy", user
                    ),
                    post.getAuthor()
            );
        }

        return commentRepository.save(comment);
    }
}
