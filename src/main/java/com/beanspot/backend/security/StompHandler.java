package com.beanspot.backend.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String jwt = accessor.getFirstNativeHeader("Authorization");

            if (jwt == null || !jwt.startsWith("Bearer ")) {
                throw new MessageDeliveryException("인증 헤더가 누락되었습니다.");
            }

            jwt = jwt.substring(7);
            if (!tokenProvider.validateToken(jwt)) {
                throw new MessageDeliveryException("유효하지 않은 토큰입니다.");
            }

            Authentication authentication = tokenProvider.getAuthentication(jwt);
            accessor.setUser(authentication);
            log.info("웹소켓 인증 성공: {}", authentication.getName());
        }

        return message;
    }
}
