package com.localzero.repository;

import com.localzero.dto.UserSummaryResponse;
import com.localzero.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByEmail(String email);
    Optional<User> findUserById(Long userId);
}
