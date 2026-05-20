package com.beanspot.backend.controller;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.common.exception.ExceptionDto;
import com.beanspot.backend.dto.chat.ChatMessageDto;
import com.beanspot.backend.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
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

        ChatMessageDto response = chatService.saveMessage(message, principal.getName());
        messagingTemplate.convertAndSend("/sub/chat/room/" + response.getRoomId(), response);
    }

    @MessageExceptionHandler
    public void handleMessageException(Exception e, Principal principal) {
        log.error("STOMP 메시지 처리 오류 - user: {}, error: {}", principal != null ? principal.getName() : "unknown", e.getMessage());
        if (principal != null) {
            ExceptionDto error = (e instanceof CustomException ce)
                    ? ExceptionDto.of(ce.getErrorCode())
                    : ExceptionDto.of(ErrorCode.INTERNAL_SERVER_ERROR);
            messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/errors", error);
        }
    }

    @GetMapping("/api/chat/rooms/{roomId}/messages")
    public List<ChatMessageDto> getChatMessages(
            @PathVariable Long roomId,
            @RequestParam(required = false) Long lastMessageId,
            @RequestParam(defaultValue = "30") int size,
            Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        return chatService.getChatMessages(roomId, userId, lastMessageId, size);
    }
}
