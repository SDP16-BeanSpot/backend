package com.beanspot.backend.entity.chat;

import com.beanspot.backend.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_room")
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long announcementId;

    private String roomName;

    private String lastMsgContent;

    private LocalDateTime lastMsgAt;

    @Builder.Default
    private Integer participantCount = 0;

    public void updateLastMessage(String content) {
        this.lastMsgContent = content;
        this.lastMsgAt = LocalDateTime.now();
    }

    public void incrementParticipantCount() {
        this.participantCount++;
    }

    public void decrementParticipantCount() {
        if (this.participantCount > 0) this.participantCount--;
    }
}
