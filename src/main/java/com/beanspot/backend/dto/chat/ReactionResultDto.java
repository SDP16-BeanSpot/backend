package com.beanspot.backend.dto.chat;

import com.beanspot.backend.entity.chat.ReactionType;

public record ReactionResultDto(
        ReactionType reactionType,
        long count,
        boolean added
) {}
