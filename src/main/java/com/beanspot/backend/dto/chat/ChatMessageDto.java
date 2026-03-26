package com.beanspot.backend.dto.chat;

import com.beanspot.backend.entity.ChatMessageType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private Long messageId;
    private ChatMessageType type;
    private String roomId;
    private String sender;
    private String message;
    private Long parentMsgId;
    private int reactionCount;

}
