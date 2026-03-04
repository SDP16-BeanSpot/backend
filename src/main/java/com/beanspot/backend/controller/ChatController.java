package com.beanspot.backend.controller;

import com.beanspot.backend.dto.chat.ChatMessageDto;
import com.beanspot.backend.entity.ChatMessage;
import com.beanspot.backend.entity.ChatMessageType;
import com.beanspot.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController // 데이터 응답용
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;


    // TODO : Principal로 다시 바꾸기
//    @MessageMapping("/chat/message")
//    public void message(ChatMessageDto message, Principal principal) { // 1. Principal 추가
//        // 2. 인터셉터에서 인증된 유저 ID(Long ID 문자열) 꺼내기
//        String userId = principal.getName();
//        System.out.println("웹소켓 메시지 수신자 ID: " + userId);
//
//        System.out.println("메시지 수신 확인: " + message.getMessage());
//
//        if (ChatMessageType.ENTER.equals(message.getType())) {
//            message.setMessage(message.getMessage() + "님이 입장하셨습니다.");
//        }
//
//        // 3. 서비스에 유저 ID를 넘겨서 DB 연동 저장
//        chatService.saveMessage(message, userId);
//
//        // 4. 해당 채팅방 구독자들에게 메시지 전송
//        // /sub/chat/room/{roomId} 채널을 구독 중인 클라이언트에게 메시지 전달
//        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
//    }
    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message, SimpMessageHeaderAccessor headerAccessor) {
        // Principal 대신 headerAccessor에서 직접 user를 꺼냄
        Authentication authentication = (Authentication) headerAccessor.getUser();

        if (authentication == null) {
            log.error("인증 정보를 찾을 수 없습니다.");
            return;
        }

        String userId = authentication.getName();
        log.info("웹소켓 메시지 수신자 ID: {}", userId);

        chatService.saveMessage(message, userId);
    }

    @GetMapping("/api/chat/room/{roomId}/messages")
    @ResponseBody
    public List<ChatMessageDto> getChatMessages(@PathVariable Long roomId) {
        return chatService.getChatMessages(roomId);
    }

}
