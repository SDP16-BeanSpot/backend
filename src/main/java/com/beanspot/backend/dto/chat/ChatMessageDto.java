package com.beanspot.backend.dto.chat;

import com.beanspot.backend.entity.chat.ChatMessageType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatMessageDto {

    private Long messageId;
    private ChatMessageType msgType;
    private Long roomId;
    private String sender;
    private String content;
    private Long parentMsgId;
    private int reactionCount;
    private List<ReactionSummaryDto> reactions;
    private LocalDateTime createdAt;

    public ChatMessageDto withSenderAndId(String sender, Long messageId) {
        return ChatMessageDto.builder()
                .messageId(messageId)
                .msgType(this.msgType)
                .roomId(this.roomId)
                .sender(sender)
                .content(this.content)
                .parentMsgId(this.parentMsgId)
                .reactionCount(this.reactionCount)
                .reactions(List.of())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
