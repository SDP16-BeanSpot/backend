package com.beanspot.backend.dto.chat;

import com.beanspot.backend.entity.ChatMessageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    @Enumerated(EnumType.STRING)
    private ChatMessageType type;
    private String roomId;
    private String sender;
    private String message;

}
