package com.beanspot.backend.repository;

import com.beanspot.backend.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 특정 채팅방(room_id)의 메시지들을 생성일(createdAt) 순서대로 가져옵니다.
    List<ChatMessage> findByChatRoomIdOrderByCreatedAtAsc(Long roomId);

    // 방 ID로 메시지를 최신순으로 끊어서 가져오기
    Slice<ChatMessage> findByChatRoomIdOrderByCreatedAtDesc(Long roomId, Pageable pageable);
}