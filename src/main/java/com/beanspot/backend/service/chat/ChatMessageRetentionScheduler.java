package com.beanspot.backend.service.chat;

import com.beanspot.backend.repository.chat.ChatMessageReactionRepository;
import com.beanspot.backend.repository.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageRetentionScheduler {

    private static final int CHUNK_SIZE = 1000;

    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageReactionRepository chatMessageReactionRepository;

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void deleteExpiredMessages() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        int totalReactions = 0, totalMessages = 0, deleted;

        do {
            deleted = chatMessageReactionRepository.deleteReactionsForExpiredMessagesChunk(cutoff, CHUNK_SIZE);
            totalReactions += deleted;
        } while (deleted == CHUNK_SIZE);

        do {
            deleted = chatMessageRepository.deleteExpiredMessagesChunk(cutoff, CHUNK_SIZE);
            totalMessages += deleted;
        } while (deleted == CHUNK_SIZE);

        log.info("[채팅 보관 정책] 7일 이전 메시지 삭제 완료 (기준: {}) - reactions: {}건, messages: {}건",
                cutoff.toLocalDate(), totalReactions, totalMessages);
    }
}
