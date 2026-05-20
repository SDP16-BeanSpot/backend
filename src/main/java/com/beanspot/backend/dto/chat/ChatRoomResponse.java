package com.beanspot.backend.dto.chat;

import com.beanspot.backend.entity.chat.ChatRoom;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomResponse {

    private Long roomId;
    private Long announcementId;
    private String roomName;
    private String lastMsgContent;
    private LocalDateTime lastMsgAt;
    private Integer participantCount;

    public static ChatRoomResponse from(ChatRoom room) {
        return ChatRoomResponse.builder()
                .roomId(room.getId())
                .announcementId(room.getAnnouncementId())
                .roomName(room.getRoomName())
                .lastMsgContent(room.getLastMsgContent())
                .lastMsgAt(room.getLastMsgAt())
                .participantCount(room.getParticipantCount())
                .build();
    }
}
