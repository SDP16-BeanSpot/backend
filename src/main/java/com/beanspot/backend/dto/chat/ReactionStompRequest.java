package com.beanspot.backend.dto.chat;

import com.beanspot.backend.entity.chat.ReactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReactionStompRequest {

    @NotNull
    private Long roomId;

    @NotNull
    private Long messageId;

    @NotNull
    private ReactionType reactionType;
}
