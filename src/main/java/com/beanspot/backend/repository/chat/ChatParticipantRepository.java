package com.beanspot.backend.repository.chat;

import com.beanspot.backend.entity.chat.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    Optional<ChatParticipant> findByChatRoomIdAndUser_Id(Long roomId, Long userId);

    @Query("SELECT cp FROM ChatParticipant cp JOIN FETCH cp.chatRoom WHERE cp.user.id = :userId ORDER BY cp.isPinned DESC, cp.pinnedAt ASC NULLS LAST, cp.chatRoom.lastMsgAt DESC NULLS LAST")
    List<ChatParticipant> findAllByUserIdWithRoom(@Param("userId") Long userId);
}