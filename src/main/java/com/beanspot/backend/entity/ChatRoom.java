package com.beanspot.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_room")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long announcementId; // 공고 ID 연관관계

    private String roomName; // 공고명을 복사해서 저장

    private String lastMsgContent; // 성능을 위한 마지막 메시지 저장

    private LocalDateTime lastMsgAt;

    private Integer participantCount = 0;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public ChatRoom(Long announcementId, String roomName) {
        this.announcementId = announcementId;
        this.roomName = roomName;
    }

    // 메시지 업데이트 메서드
    public void updateLastMessage(String content) {
        this.lastMsgContent = content;
        this.lastMsgAt = LocalDateTime.now();
    }

}
