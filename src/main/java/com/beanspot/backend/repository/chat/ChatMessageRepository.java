package com.beanspot.backend.repository.chat;

import com.beanspot.backend.dto.chat.UnreadCountProjection;
import com.beanspot.backend.entity.chat.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m JOIN FETCH m.sender WHERE m.chatRoom.id = :roomId ORDER BY m.id DESC")
    Slice<ChatMessage> findByChatRoomIdOrderByIdDesc(@Param("roomId") Long roomId, Pageable pageable);

    @Query("SELECT m FROM ChatMessage m JOIN FETCH m.sender WHERE m.chatRoom.id = :roomId AND m.id < :lastMessageId ORDER BY m.id DESC")
    Slice<ChatMessage> findByChatRoomIdAndIdLessThanOrderByIdDesc(@Param("roomId") Long roomId, @Param("lastMessageId") Long lastMessageId, Pageable pageable);

    @Query("SELECT m FROM ChatMessage m JOIN FETCH m.sender WHERE m.id = :messageId")
    Optional<ChatMessage> findByIdWithSender(@Param("messageId") Long messageId);

    Optional<ChatMessage> findTopByChatRoomIdOrderByIdDesc(Long roomId);

    @Query("SELECT m FROM ChatMessage m JOIN FETCH m.sender " +
           "WHERE m.chatRoom.id = :roomId AND m.createdAt < :cutoff " +
           "ORDER BY m.id DESC")
    Slice<ChatMessage> findArchiveByChatRoomId(@Param("roomId") Long roomId,
                                               @Param("cutoff") LocalDateTime cutoff,
                                               Pageable pageable);

    @Query("SELECT m FROM ChatMessage m JOIN FETCH m.sender " +
           "WHERE m.chatRoom.id = :roomId AND m.createdAt < :cutoff AND m.id < :lastMessageId " +
           "ORDER BY m.id DESC")
    Slice<ChatMessage> findArchiveByChatRoomIdAndIdLessThan(@Param("roomId") Long roomId,
                                                            @Param("cutoff") LocalDateTime cutoff,
                                                            @Param("lastMessageId") Long lastMessageId,
                                                            Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM chat_message WHERE id IN " +
                   "(SELECT id FROM (SELECT m.id FROM chat_message m " +
                   "LEFT JOIN report r ON m.id = r.message_id " +
                   "WHERE m.created_at < :cutoff AND r.id IS NULL " +
                   "ORDER BY m.id ASC LIMIT :chunkSize) AS tmp)",
           nativeQuery = true)
    int deleteExpiredMessagesChunk(@Param("cutoff") LocalDateTime cutoff,
                                   @Param("chunkSize") int chunkSize);

    @Query("SELECT m.chatRoom.id AS roomId, COUNT(m.id) AS unreadCount " +
           "FROM ChatMessage m " +
           "JOIN ChatParticipant cp ON m.chatRoom.id = cp.chatRoom.id " +
           "WHERE cp.user.id = :userId " +
           "AND (cp.lastReadMsgId IS NULL OR m.id > cp.lastReadMsgId) " +
           "GROUP BY m.chatRoom.id")
    List<UnreadCountProjection> countUnreadMessagesByUserId(@Param("userId") Long userId);
}
