package com.beanspot.backend.dto.chat;

import com.beanspot.backend.entity.chat.ChatRoom;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private long unreadCount;
    @JsonIgnore
    private boolean isNewParticipant;

    public static ChatRoomResponse from(ChatRoom room, boolean isNewParticipant) {
        return ChatRoomResponse.builder()
                .roomId(room.getId())
                .announcementId(room.getAnnouncementId())
                .roomName(room.getRoomName())
                .lastMsgContent(room.getLastMsgContent())
                .lastMsgAt(room.getLastMsgAt())
                .participantCount(room.getParticipantCount())
                .isNewParticipant(isNewParticipant)
                .build();
    }

    public static ChatRoomResponse from(ChatRoom room, boolean isNewParticipant, long unreadCount) {
        return ChatRoomResponse.builder()
                .roomId(room.getId())
                .announcementId(room.getAnnouncementId())
                .roomName(room.getRoomName())
                .lastMsgContent(room.getLastMsgContent())
                .lastMsgAt(room.getLastMsgAt())
                .participantCount(room.getParticipantCount())
                .unreadCount(unreadCount)
                .isNewParticipant(isNewParticipant)
                .build();
    }
}
