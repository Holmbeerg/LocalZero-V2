package com.localzero.repository;

import com.localzero.model.InitiativeMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface InitiativeMemberRepository extends JpaRepository<InitiativeMember, Long> {

    @Modifying
    @Query(value = "INSERT INTO initiative_members (initiative_id, user_id) VALUES (?1, ?2)",
            nativeQuery = true)
    void insertMembership(Long initiativeId, Long userId);
}


