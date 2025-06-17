package com.localzero.repository;

import com.localzero.model.Initiative;
import com.localzero.model.User;
import com.localzero.model.enums.Neighborhood;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InitiativeRepository extends JpaRepository<Initiative, Long> {
    @EntityGraph(attributePaths = {"creator", "participants"})
    @Query("""
    SELECT DISTINCT i FROM Initiative i
    LEFT JOIN InitiativeMember im ON i.id = im.initiative.id
    WHERE i.publicFlag = true
       OR i.creator = :user
       OR (im.user = :user)
       OR (i.publicFlag = false AND i.location = :userNeighborhood)
    """)
    List<Initiative> findAllAccessibleByUser(@Param("user") User user,
                                             @Param("userNeighborhood") Neighborhood userNeighborhood);

    @EntityGraph(attributePaths = {"creator", "participants"})
    @Query("""
            SELECT DISTINCT i FROM Initiative i
            LEFT JOIN InitiativeMember im ON i.id = im.initiative.id
            WHERE i.id = :initiativeId AND (
                i.publicFlag = true
                OR i.creator = :user
                OR (im.user = :user)
                OR (i.publicFlag = false AND i.location = :userNeighborhood)
            )
            """)
    Optional<Initiative> findAccessibleById(@Param("initiativeId") Long id,
                                            @Param("user") User user,
                                            @Param("userNeighborhood") Neighborhood userNeighborhood);
}
