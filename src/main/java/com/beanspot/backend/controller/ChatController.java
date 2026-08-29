package com.beanspot.backend.controller;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.common.exception.ExceptionDto;
import com.beanspot.backend.common.response.ApiResponse;
import com.beanspot.backend.dto.chat.ChatMessageDto;
import com.beanspot.backend.dto.chat.ChatParticipantResponse;
import com.beanspot.backend.dto.chat.ChatRoomCreateRequest;
import com.beanspot.backend.dto.chat.ChatRoomResponse;
import com.beanspot.backend.dto.chat.ReactionBroadcastDto;
import com.beanspot.backend.dto.chat.ReactionResultDto;
import com.beanspot.backend.dto.chat.ReactionStompRequest;
import com.beanspot.backend.service.chat.ReactionService;
import com.beanspot.backend.entity.chat.ChatMessageType;
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
    private final ReactionService reactionService;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message, Principal principal) {
        if (principal == null) {
            log.error("인증 정보를 찾을 수 없습니다.");
            return;
        }

        ChatMessageDto response = chatService.saveMessage(message, principal.getName());
        messagingTemplate.convertAndSend("/sub/chat/room/" + response.getRoomId(), response);
    }

    @MessageMapping("/chat/reaction")
    public void reaction(ReactionStompRequest request, Principal principal) {
        if (principal == null) {
            log.error("인증 정보를 찾을 수 없습니다.");
            return;
        }

        Long userId = Long.parseLong(principal.getName());
        ReactionResultDto result = reactionService.toggleReaction(userId, request.getMessageId(), request.getReactionType());

        messagingTemplate.convertAndSend(
                "/sub/chat/room/" + request.getRoomId(),
                ReactionBroadcastDto.of(request.getMessageId(), result.reactionType(), result.count(), userId, result.added())
        );
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
        ChatRoomResponse response = chatService.joinChatRoom(userId, request);
        if (response.isNewParticipant()) {
            ChatMessageDto enterMsg = ChatMessageDto.builder()
                    .msgType(ChatMessageType.ENTER)
                    .roomId(response.getRoomId())
                    .content(chatService.getUserNickname(userId) + "님이 입장하셨습니다.")
                    .build();
            messagingTemplate.convertAndSend("/sub/chat/room/" + response.getRoomId(), enterMsg);
        }
        return ApiResponse.created(response);
    }

    @Operation(summary = "채팅방 핀 토글", description = "채팅방을 목록 상단에 고정하거나 해제합니다.")
    @PatchMapping("/rooms/{roomId}/pin")
    public ApiResponse<ChatParticipantResponse> togglePin(
            @PathVariable Long roomId,
            @CurrentUserId Long userId) {
        return ApiResponse.ok(chatService.togglePin(userId, roomId));
    }

    @Operation(summary = "채팅방 알림 토글", description = "채팅방 알림을 켜거나 끕니다.")
    @PatchMapping("/rooms/{roomId}/notification")
    public ApiResponse<ChatParticipantResponse> toggleNotification(
            @PathVariable Long roomId,
            @CurrentUserId Long userId) {
        return ApiResponse.ok(chatService.toggleNotification(userId, roomId));
    }

    @Operation(summary = "채팅방 나가기", description = "채팅방에서 나갑니다. 참여자 목록에서 제거되며 퇴장 메시지가 브로드캐스트됩니다.")
    @DeleteMapping("/rooms/{roomId}/leave")
    public ApiResponse<Void> leaveChatRoom(
            @PathVariable Long roomId,
            @CurrentUserId Long userId) {
        String nickname = chatService.getUserNickname(userId);
        chatService.leaveChatRoom(userId, roomId);
        ChatMessageDto quitMsg = ChatMessageDto.builder()
                .msgType(ChatMessageType.QUIT)
                .roomId(roomId)
                .content(nickname + "님이 나가셨습니다.")
                .build();
        messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, quitMsg);
        return ApiResponse.ok(null);
    }

    @Operation(summary = "채팅 메시지 목록 조회", description = "커서 기반 페이지네이션. lastMessageId 없으면 최신부터, 있으면 해당 ID 이전 메시지를 조회합니다.")
    @GetMapping("/rooms/{roomId}/messages")
    public ApiResponse<List<ChatMessageDto>> getChatMessages(
            @PathVariable Long roomId,
            @RequestParam(required = false) Long lastMessageId,
            @RequestParam(defaultValue = "30") int size,
            @CurrentUserId Long userId) {
        return ApiResponse.ok(chatService.getChatMessages(roomId, userId, lastMessageId, size));
    }

    @Operation(summary = "7일 이전 메시지 조회 (클라이언트 동기화용)",
               description = "서버에서 삭제 예정인 7일 이전 메시지를 조회합니다. 클라이언트 로컬 DB 동기화에 사용하세요.")
    @GetMapping("/rooms/{roomId}/messages/archive")
    public ApiResponse<List<ChatMessageDto>> getArchivedMessages(
            @PathVariable Long roomId,
            @RequestParam(required = false) Long lastMessageId,
            @RequestParam(defaultValue = "50") int size,
            @CurrentUserId Long userId) {
        return ApiResponse.ok(chatService.getArchivedMessages(roomId, userId, lastMessageId, size));
    }
}
