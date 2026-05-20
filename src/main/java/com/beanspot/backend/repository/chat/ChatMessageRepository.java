package com.beanspot.backend.repository.chat;

import com.beanspot.backend.entity.chat.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m JOIN FETCH m.sender WHERE m.chatRoom.id = :roomId ORDER BY m.id DESC")
    Slice<ChatMessage> findByChatRoomIdOrderByIdDesc(@Param("roomId") Long roomId, Pageable pageable);

    @Query("SELECT m FROM ChatMessage m JOIN FETCH m.sender WHERE m.chatRoom.id = :roomId AND m.id < :lastMessageId ORDER BY m.id DESC")
    Slice<ChatMessage> findByChatRoomIdAndIdLessThanOrderByIdDesc(@Param("roomId") Long roomId, @Param("lastMessageId") Long lastMessageId, Pageable pageable);

    @Query("SELECT m FROM ChatMessage m JOIN FETCH m.sender WHERE m.id = :messageId")
    Optional<ChatMessage> findByIdWithSender(@Param("messageId") Long messageId);
}
