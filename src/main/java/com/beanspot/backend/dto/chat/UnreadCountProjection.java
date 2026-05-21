package com.beanspot.backend.dto.chat;

public interface UnreadCountProjection {
    Long getRoomId();
    Long getUnreadCount();
}
