package com.localzero.repository;

import com.localzero.model.Post;
import com.localzero.model.User;
import com.localzero.model.enums.Neighborhood;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = {"comments"})
    @Query("""
            SELECT DISTINCT p FROM Post p
            LEFT JOIN p.initiative i
            LEFT JOIN i.participants im
            WHERE p.id = :postId
            AND (i.publicFlag = true
                 OR i.creator = :user
                 OR im.user = :user
                 OR (i.publicFlag = false AND i.location = :userNeighborhood))
            """)
    Optional<Post> findAccessibleById(@Param ("postId") Long id,
                                      @Param("user") User user,
                                      @Param("userNeighborhood") Neighborhood userNeighborhood);
}
