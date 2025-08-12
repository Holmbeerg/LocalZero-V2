package com.localzero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.localzero.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
