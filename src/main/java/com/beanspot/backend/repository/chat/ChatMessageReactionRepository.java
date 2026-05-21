package com.beanspot.backend.repository.chat;

import com.beanspot.backend.entity.chat.ChatMessageReaction;
import com.beanspot.backend.entity.chat.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.beanspot.backend.dto.chat.ReactionMappingDto;

import java.util.List;
import java.util.Optional;

public interface ChatMessageReactionRepository extends JpaRepository<ChatMessageReaction, Long> {
    Optional<ChatMessageReaction> findByChatMessage_IdAndUser_IdAndReactionType(Long messageId, Long userId, ReactionType reactionType);

    @Query("SELECT new com.beanspot.backend.dto.chat.ReactionMappingDto(r.chatMessage.id, r.reactionType, r.user.id) " +
           "FROM ChatMessageReaction r WHERE r.chatMessage.id IN :messageIds")
    List<ReactionMappingDto> findReactionMappingsByMessageIds(@Param("messageIds") List<Long> messageIds);

    long countByChatMessage_IdAndReactionType(Long messageId, ReactionType reactionType);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ChatMessage m SET m.reactionCount = m.reactionCount + 1 WHERE m.id = :messageId")
    void incrementReactionCount(@Param("messageId") Long messageId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ChatMessage m SET m.reactionCount = CASE WHEN m.reactionCount > 0 THEN m.reactionCount - 1 ELSE 0 END WHERE m.id = :messageId")
    void decrementReactionCount(@Param("messageId") Long messageId);
}
