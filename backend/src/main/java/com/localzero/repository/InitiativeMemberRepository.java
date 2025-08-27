package com.localzero.repository;

import com.localzero.model.InitiativeMember;
import com.localzero.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InitiativeMemberRepository extends JpaRepository<InitiativeMember, Long> {

    @Query("select im.initiative.id from InitiativeMember im where im.user.userId = :userId")
    List<Long> findInitiativeIdsByUserId(@Param("userId") Long userId);

    @Query("select im.user from InitiativeMember im where im.initiative.id = :initiativeId")
    List<User> findUsersByInitiativeId(@Param("initiativeId") Long initiativeId);
}
