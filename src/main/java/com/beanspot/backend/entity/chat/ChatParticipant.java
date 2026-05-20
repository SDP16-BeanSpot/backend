package com.beanspot.backend.entity.chat;

import com.beanspot.backend.entity.BaseEntity;
import com.beanspot.backend.entity.User;
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
@Table(name = "chat_participant")
public class ChatParticipant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String role;

    @Builder.Default
    private LocalDateTime enteredAt = LocalDateTime.now();

    @Builder.Default
    private boolean isNotified = true;

    private Long lastReadMsgId;

    @Builder.Default
    private boolean isPinned = false;

    private LocalDateTime pinnedAt;

    public void togglePin() {
        this.isPinned = !this.isPinned;
        this.pinnedAt = this.isPinned ? LocalDateTime.now() : null;
    }

    public void toggleNotification() {
        this.isNotified = !this.isNotified;
    }
}
