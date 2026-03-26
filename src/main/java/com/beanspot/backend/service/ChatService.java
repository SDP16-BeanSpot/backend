package com.beanspot.backend.service;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.dto.chat.ChatMessageDto;
import com.beanspot.backend.entity.ChatMessage;
import com.beanspot.backend.entity.ChatRoom;
import com.beanspot.backend.entity.User;
import com.beanspot.backend.repository.ChatMessageRepository;
import com.beanspot.backend.repository.ChatRoomRepository;
import com.beanspot.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    /**
     * 채팅 메시지 저장
     */
    @Transactional
    public String saveMessage(ChatMessageDto messageDto, String userId) {
        User sender = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ChatRoom room = chatRoomRepository.findById(Long.parseLong(messageDto.getRoomId()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        ChatMessage messageEntity = ChatMessage.builder()
                .chatRoom(room)
                .sender(sender)
                .content(messageDto.getMessage())
                .msgType(messageDto.getType())
                .build();

        ChatMessage saved = chatMessageRepository.save(messageEntity);
        room.updateLastMessage(messageDto.getMessage());
        messageDto.setMessageId(saved.getId());

        return sender.getNickname();
    }

    /**
     * 채팅방 메시지 목록 조회 (커서 기반)
     * lastMessageId가 null이면 최신 메시지부터, 있으면 해당 ID 이전 메시지부터 조회
     */
    public List<ChatMessageDto> getChatMessages(Long roomId, Long lastMessageId, int size) {
        Pageable pageable = PageRequest.of(0, size);

        Slice<ChatMessage> slice = (lastMessageId == null)
                ? chatMessageRepository.findByChatRoomIdOrderByIdDesc(roomId, pageable)
                : chatMessageRepository.findByChatRoomIdAndIdLessThanOrderByIdDesc(roomId, lastMessageId, pageable);

        List<ChatMessageDto> messages = slice.getContent().stream()
                .map(entity -> ChatMessageDto.builder()
                        .messageId(entity.getId())
                        .type(entity.getMsgType())
                        .roomId(entity.getChatRoom().getId().toString())
                        .sender(entity.getSender().getNickname())
                        .message(entity.getContent())
                        .parentMsgId(entity.getParentMsgId())
                        .reactionCount(entity.getReactionCount())
                        .build())
                .collect(Collectors.toList());

        // 오래된 순으로 정렬해서 반환 (클라이언트 렌더링 편의)
        Collections.reverse(messages);
        return messages;
    }
}
