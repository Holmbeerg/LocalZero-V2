package com.localzero.repository;

import com.localzero.model.Like;
import com.localzero.model.Post;
import com.localzero.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, User user);
}
