package com.beanspot.backend.repository.chat;

import com.beanspot.backend.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 특정 공고 ID(announcement_id)와 연결된 채팅방 조회
    Optional<ChatRoom> findByAnnouncementId(Long announcementId);

}
