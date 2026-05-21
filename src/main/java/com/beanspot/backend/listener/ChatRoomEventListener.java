package com.beanspot.backend.listener;

import com.beanspot.backend.entity.announcement.Announcement;
import com.beanspot.backend.entity.chat.ChatRoom;
import com.beanspot.backend.repository.announcement.AnnouncementRepository;
import com.beanspot.backend.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRoomEventListener {

    private final AnnouncementRepository announcementRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAnnouncementCreated(AnnouncementCreatedEvent event) {
        Announcement announcement = announcementRepository.findById(event.announcementId())
                .orElseThrow();

        chatRoomRepository.save(ChatRoom.builder()
                .announcementId(announcement.getId())
                .roomName(announcement.getTitle())
                .build());

        log.info("채팅방 자동 생성 완료 - announcementId: {}, title: {}", announcement.getId(), announcement.getTitle());
    }
}
