package com.beanspot.backend.repository.chat;

import com.beanspot.backend.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByAnnouncementId(Long announcementId);

    @Modifying
    @Query("UPDATE ChatRoom r SET r.participantCount = r.participantCount + 1 WHERE r.id = :roomId")
    void incrementParticipantCount(@Param("roomId") Long roomId);
}
