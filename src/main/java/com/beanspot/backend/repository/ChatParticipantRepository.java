package com.beanspot.backend.repository;

import com.beanspot.backend.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    // 특정 유저가 특정 채팅방에 참여 중인지 확인합니다.
    Optional<ChatParticipant> findByChatRoomIdAndUserId(Long roomId, Long userId);

}