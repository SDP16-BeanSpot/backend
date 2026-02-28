package com.beanspot.backend.controller;

import com.beanspot.backend.dto.chat.ChatMessageDto;
import com.beanspot.backend.entity.ChatMessage;
import com.beanspot.backend.entity.ChatMessageType;
import com.beanspot.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message) {
        System.out.println("메시지 수신 확인: " + message.getMessage());

        if (ChatMessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getMessage() + "님이 입장하셨습니다.");
        }

        // DB 저장
        chatService.saveMessage(message);

        // /sub/chat/room/{roomId} 채널을 구독 중인 클라이언트에게 메시지 전달
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @GetMapping("/api/chat/room/{roomId}/messages")
    @ResponseBody
    public List<ChatMessageDto> getChatMessages(@PathVariable Long roomId) {
        return chatService.getChatMessages(roomId);
    }

}
