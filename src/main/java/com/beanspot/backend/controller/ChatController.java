package com.beanspot.backend.controller;

import com.beanspot.backend.dto.chat.ChatMessageDto;
import com.beanspot.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message, Principal principal) {
        if (principal == null) {
            log.error("인증 정보를 찾을 수 없습니다.");
            return;
        }

        String userId = principal.getName();
        log.info("웹소켓 메시지 수신자 ID: {}", userId);

        String senderNickname = chatService.saveMessage(message, userId);
        message.setSender(senderNickname);
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @GetMapping("/api/chat/rooms/{roomId}/messages")
    public List<ChatMessageDto> getChatMessages(
            @PathVariable Long roomId,
            @RequestParam(required = false) Long lastMessageId,
            @RequestParam(defaultValue = "30") int size) {
        return chatService.getChatMessages(roomId, lastMessageId, size);
    }

}
