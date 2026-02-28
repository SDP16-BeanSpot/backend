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

    // StompHandler.java 수정
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // 1. StompHeaderAccessor.wrap 대신 MessageHeaderAccessor를 사용
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String jwt = accessor.getFirstNativeHeader("Authorization");

            if (jwt != null && jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
                if (tokenProvider.validateToken(jwt)) {
                    Authentication authentication = tokenProvider.getAuthentication(jwt);

                    // 2. 여기서 유저를 심어줌
                    accessor.setUser(authentication);
                    log.info("웹소켓 인증 성공: {}", authentication.getName());
                }
            }
        }
        return message;
    }
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        // 1. 연결 시점(CONNECT)에만 토큰 검증
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//            String jwt = accessor.getFirstNativeHeader("Authorization");
//            log.info("웹소켓 연결 시도 - 토큰 확인: {}", jwt);
//
//            if (jwt != null && jwt.startsWith("Bearer ")) {
//                jwt = jwt.substring(7);
//
//                // 2. 토큰 유효성 검사 (아까 에러 났던 서명 확인 포함)
//                if (tokenProvider.validateToken(jwt)) {
//                    // 3. 토큰에서 유저 정보(Authentication)를 뽑아 세션에 저장
//                    Authentication authentication = tokenProvider.getAuthentication(jwt);
//                    accessor.setUser(authentication);
//                    log.info("웹소켓 인증 성공: {}", authentication.getName());
//                } else {
//                    throw new MessageDeliveryException("유효하지 않은 토큰입니다.");
//                }
//            } else {
//                throw new MessageDeliveryException("인증 헤더가 누락되었습니다.");
//            }
//        }
//        return message;
//    }
}