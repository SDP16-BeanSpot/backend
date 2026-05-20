package com.beanspot.backend.repository.chat;

import com.beanspot.backend.entity.chat.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    Optional<ChatParticipant> findByChatRoomIdAndUser_Id(Long roomId, Long userId);

}