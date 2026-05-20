package com.beanspot.backend.service.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompEventListener {

    private static final String ROOM_DEST_PREFIX = "/sub/chat/room/";

    private final ChatService chatService;

    // sessionId -> subscriptionId -> SubscriptionInfo(roomId, userId)
    private final ConcurrentHashMap<String, Map<String, SubscriptionInfo>> subscriptions = new ConcurrentHashMap<>();

    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String dest = accessor.getDestination();
        if (dest == null || !dest.startsWith(ROOM_DEST_PREFIX)) return;

        String sessionId = accessor.getSessionId();
        String subId = accessor.getSubscriptionId();
        if (sessionId == null || subId == null || accessor.getUser() == null) return;

        try {
            Long roomId = Long.parseLong(dest.substring(ROOM_DEST_PREFIX.length()));
            Long userId = Long.parseLong(accessor.getUser().getName());
            SubscriptionInfo info = new SubscriptionInfo(roomId, userId);
            subscriptions
                    .computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>())
                    .put(subId, info);
            saveLastRead(info);
        } catch (NumberFormatException e) {
            log.warn("구독 경로 파싱 실패: {}", dest);
        }
    }

    @EventListener
    public void handleUnsubscribe(SessionUnsubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String subId = accessor.getSubscriptionId();
        if (sessionId == null || subId == null) return;

        Map<String, SubscriptionInfo> subs = subscriptions.get(sessionId);
        if (subs == null) return;

        SubscriptionInfo info = subs.remove(subId);
        if (info != null) {
            saveLastRead(info);
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        if (sessionId == null) return;

        Map<String, SubscriptionInfo> subs = subscriptions.remove(sessionId);
        if (subs == null) return;

        subs.values().forEach(this::saveLastRead);
    }

    private void saveLastRead(SubscriptionInfo info) {
        try {
            chatService.saveLastReadMsgId(info.userId(), info.roomId());
        } catch (Exception e) {
            log.error("lastReadMsgId 저장 실패 - userId: {}, roomId: {}", info.userId(), info.roomId(), e);
        }
    }

    private record SubscriptionInfo(Long roomId, Long userId) {}
}
