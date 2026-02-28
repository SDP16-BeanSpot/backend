package com.beanspot.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_message_reaction") // 테이블 이름도 맞춰주면 좋습니다!
public class ChatMessageReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "msg_id") // DB 컬럼명도 명확하게!
    private ChatMessage chatMessage;

    private Long userId; // 반응을 남긴 유저 ID

    private String reactionType; // LIKE, HEART, CHECK 등 (혹은 전용 Enum도 좋음)

    @Builder
    public ChatMessageReaction(ChatMessage chatMessage, Long userId, String reactionType) {
        this.chatMessage = chatMessage;
        this.userId = userId;
        this.reactionType = reactionType;
    }

}