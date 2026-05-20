package com.beanspot.backend.dto.chat;

import com.beanspot.backend.entity.chat.ReactionType;

public record ReactionMappingDto(
        Long messageId,
        ReactionType reactionType,
        Long userId
) {}
