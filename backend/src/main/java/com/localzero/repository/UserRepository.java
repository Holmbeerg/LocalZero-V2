package com.localzero.repository;

import com.localzero.model.User;
import com.localzero.model.enums.Neighborhood;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.location = :location")
    List<User> findByLocation(@Param("location") Neighborhood location);
}
