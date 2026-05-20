package com.beanspot.backend.controller;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.common.exception.ExceptionDto;
import com.beanspot.backend.common.response.ApiResponse;
import com.beanspot.backend.dto.chat.ChatMessageDto;
import com.beanspot.backend.dto.chat.ChatRoomCreateRequest;
import com.beanspot.backend.dto.chat.ChatRoomResponse;
import com.beanspot.backend.service.chat.ChatService;
import com.beanspot.backend.security.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@Tag(name = "채팅 API", description = "채팅방 생성/입장, 목록 조회, 메시지 조회 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
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

    @Operation(summary = "내 채팅방 목록 조회", description = "참여 중인 채팅방 목록을 최근 메시지 순으로 반환합니다.")
    @GetMapping("/rooms")
    public ApiResponse<List<ChatRoomResponse>> getChatRooms(@CurrentUserId Long userId) {
        return ApiResponse.ok(chatService.getChatRooms(userId));
    }

    @Operation(summary = "채팅방 입장", description = "공고 ID에 해당하는 채팅방에 입장합니다. 채팅방은 공고 등록 시 자동으로 생성됩니다.")
    @PostMapping("/rooms")
    public ApiResponse<ChatRoomResponse> joinChatRoom(
            @Valid @RequestBody ChatRoomCreateRequest request,
            @CurrentUserId Long userId) {
        return ApiResponse.created(chatService.joinChatRoom(userId, request));
    }

    @Operation(summary = "채팅 메시지 목록 조회", description = "커서 기반 페이지네이션. lastMessageId 없으면 최신부터, 있으면 해당 ID 이전 메시지를 조회합니다.")
    @GetMapping("/rooms/{roomId}/messages")
    public List<ChatMessageDto> getChatMessages(
            @PathVariable Long roomId,
            @RequestParam(required = false) Long lastMessageId,
            @RequestParam(defaultValue = "30") int size,
            @CurrentUserId Long userId) {
        return chatService.getChatMessages(roomId, userId, lastMessageId, size);
    }
}
