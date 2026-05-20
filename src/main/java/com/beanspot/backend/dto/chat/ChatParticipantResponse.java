package com.beanspot.backend.dto.chat;

import com.beanspot.backend.entity.chat.ChatParticipant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatParticipantResponse {

    private Long roomId;
    private boolean isPinned;
    private boolean isNotified;

    public static ChatParticipantResponse from(ChatParticipant participant) {
        return ChatParticipantResponse.builder()
                .roomId(participant.getChatRoom().getId())
                .isPinned(participant.isPinned())
                .isNotified(participant.isNotified())
                .build();
    }
}
