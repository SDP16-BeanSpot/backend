package com.beanspot.backend.service.chat;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.dto.chat.ReactionResultDto;
import com.beanspot.backend.entity.User;
import com.beanspot.backend.entity.chat.ChatMessage;
import com.beanspot.backend.entity.chat.ChatMessageReaction;
import com.beanspot.backend.entity.chat.ReactionType;
import com.beanspot.backend.repository.UserRepository;
import com.beanspot.backend.repository.chat.ChatMessageReactionRepository;
import com.beanspot.backend.repository.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReactionService {

    private final ChatMessageReactionRepository reactionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReactionResultDto toggleReaction(Long userId, Long messageId, ReactionType reactionType) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_MESSAGE_NOT_FOUND));

        if (message.isDeleted()) {
            throw new CustomException(ErrorCode.CHAT_MESSAGE_NOT_FOUND);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Optional<ChatMessageReaction> existing = reactionRepository
                .findByChatMessage_IdAndUser_IdAndReactionType(messageId, userId, reactionType);

        boolean added;
        if (existing.isPresent()) {
            reactionRepository.delete(existing.get());
            reactionRepository.decrementReactionCount(messageId);
            added = false;
        } else {
            reactionRepository.save(ChatMessageReaction.builder()
                    .chatMessage(message)
                    .user(user)
                    .reactionType(reactionType)
                    .build());
            reactionRepository.incrementReactionCount(messageId);
            added = true;
        }

        long count = reactionRepository.countByChatMessage_IdAndReactionType(messageId, reactionType);
        return new ReactionResultDto(reactionType, count, added);
    }
}
