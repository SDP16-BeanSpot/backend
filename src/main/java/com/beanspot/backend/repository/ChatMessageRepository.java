package com.beanspot.backend.repository;

import com.beanspot.backend.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 최신 메시지부터 size개 가져오기 (초기 로딩)
    Slice<ChatMessage> findByChatRoomIdOrderByIdDesc(Long roomId, Pageable pageable);

    // lastMessageId 이전 메시지 가져오기 (스크롤 업)
    Slice<ChatMessage> findByChatRoomIdAndIdLessThanOrderByIdDesc(Long roomId, Long lastMessageId, Pageable pageable);
}