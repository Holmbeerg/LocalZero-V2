package com.localzero.repository;

import com.localzero.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MessagesRepository extends JpaRepository<Message, Long> {

    @Modifying
    @Query(value = "INSERT INTO messages (sender_id, receiver_id, text) VALUES (?1, ?2, ?3)",
            nativeQuery = true)
    void insertMessage(Long sender_id, Long receiver_id, String text);
}


