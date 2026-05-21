package com.beanspot.backend.dto.chat;

import com.beanspot.backend.entity.chat.ReactionType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReactionSummaryDto {
    private ReactionType reactionType;
    private long count;
    private boolean reacted;
}
