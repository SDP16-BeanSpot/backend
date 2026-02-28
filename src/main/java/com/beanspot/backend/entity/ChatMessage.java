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
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

//    private Long senderId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id") // DB 테이블의 컬럼명
    private User sender; // 작성자 정보 (User 엔티티와 연결)

    private Long parentMsgId; // 답글 기능을 위한 부모 메시지 ID

    @Enumerated(EnumType.STRING)
    private ChatMessageType msgType; // TALK, NOTICE

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean isDeleted = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    // 빌더 패턴 추가 -> Service에서 필요
    @Builder
    public ChatMessage(ChatRoom chatRoom, User sender, String content, ChatMessageType msgType) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = content;
        this.msgType = msgType;
    }

}