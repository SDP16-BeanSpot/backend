package com.beanspot.backend.dto.chat;

import com.beanspot.backend.entity.chat.ReactionType;

public record ReactionBroadcastDto(
        String msgType,
        Long messageId,
        ReactionType reactionType,
        long count,
        Long actorUserId,
        boolean added
) {
    public static ReactionBroadcastDto of(Long messageId, ReactionType reactionType, long count, Long actorUserId, boolean added) {
        return new ReactionBroadcastDto("REACTION", messageId, reactionType, count, actorUserId, added);
    }
}
