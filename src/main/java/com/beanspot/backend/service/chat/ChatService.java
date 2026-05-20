package com.beanspot.backend.service.chat;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.dto.chat.ChatMessageDto;
import com.beanspot.backend.dto.chat.ChatRoomCreateRequest;
import com.beanspot.backend.dto.chat.ChatRoomResponse;
import com.beanspot.backend.entity.chat.ChatMessage;
import com.beanspot.backend.entity.chat.ChatParticipant;
import com.beanspot.backend.entity.chat.ChatRoom;
import com.beanspot.backend.entity.User;
import com.beanspot.backend.repository.chat.ChatMessageRepository;
import com.beanspot.backend.repository.chat.ChatParticipantRepository;
import com.beanspot.backend.repository.chat.ChatRoomRepository;
import com.beanspot.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final UserRepository userRepository;
    @Transactional
    public ChatMessageDto saveMessage(ChatMessageDto messageDto, String userId) {
        if (messageDto.getContent() == null || messageDto.getContent().isBlank()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        Long userIdLong = Long.parseLong(userId);

        User sender = userRepository.findById(userIdLong)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ChatRoom room = chatRoomRepository.findById(messageDto.getRoomId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        chatParticipantRepository.findByChatRoomIdAndUser_Id(room.getId(), userIdLong)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_NOT_PARTICIPANT));

        ChatMessage saved = chatMessageRepository.save(
                ChatMessage.builder()
                        .chatRoom(room)
                        .sender(sender)
                        .content(messageDto.getContent())
                        .msgType(messageDto.getMsgType())
                        .build()
        );
        room.updateLastMessage(messageDto.getContent());

        return messageDto.withSenderAndId(sender.getNickname(), saved.getId());
    }

    public List<ChatMessageDto> getChatMessages(Long roomId, Long userId, Long lastMessageId, int size) {
        chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        chatParticipantRepository.findByChatRoomIdAndUser_Id(roomId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_NOT_PARTICIPANT));

        Pageable pageable = PageRequest.of(0, size);

        Slice<ChatMessage> slice = (lastMessageId == null)
                ? chatMessageRepository.findByChatRoomIdOrderByIdDesc(roomId, pageable)
                : chatMessageRepository.findByChatRoomIdAndIdLessThanOrderByIdDesc(roomId, lastMessageId, pageable);

        List<ChatMessageDto> messages = new ArrayList<>(slice.getContent().stream()
                .map(entity -> ChatMessageDto.builder()
                        .messageId(entity.getId())
                        .msgType(entity.getMsgType())
                        .roomId(entity.getChatRoom().getId())
                        .sender(entity.getSender().getNickname())
                        .content(entity.getContent())
                        .parentMsgId(entity.getParentMsgId())
                        .reactionCount(entity.getReactionCount())
                        .createdAt(entity.getCreatedAt())
                        .build())
                .toList());

        Collections.reverse(messages);
        return messages;
    }

    public List<ChatRoomResponse> getChatRooms(Long userId) {
        return chatParticipantRepository.findAllByUserIdWithRoom(userId).stream()
                .map(cp -> ChatRoomResponse.from(cp.getChatRoom()))
                .toList();
    }

    @Transactional
    public ChatRoomResponse joinChatRoom(Long userId, ChatRoomCreateRequest request) {
        ChatRoom room = chatRoomRepository.findByAnnouncementId(request.getAnnouncementId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return joinExistingChatRoom(user, room);
    }

    private ChatRoomResponse joinExistingChatRoom(User user, ChatRoom room) {
        boolean isAlreadyParticipant = chatParticipantRepository
                .findByChatRoomIdAndUser_Id(room.getId(), user.getId())
                .isPresent();

        if (!isAlreadyParticipant) {
            chatRoomRepository.incrementParticipantCount(room.getId());
            chatParticipantRepository.save(ChatParticipant.builder()
                    .chatRoom(room)
                    .user(user)
                    .role("GUEST")
                    .build());
            room = chatRoomRepository.findById(room.getId()).orElse(room);
        }

        return ChatRoomResponse.from(room);
    }
}
