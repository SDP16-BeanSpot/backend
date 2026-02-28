package com.beanspot.backend.service;

import com.beanspot.backend.dto.chat.ChatMessageDto;
import com.beanspot.backend.entity.ChatMessage;
import com.beanspot.backend.entity.ChatRoom;
import com.beanspot.backend.entity.User;
import com.beanspot.backend.repository.ChatMessageRepository;
import com.beanspot.backend.repository.ChatRoomRepository;
import com.beanspot.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void saveMessage(ChatMessageDto messageDto) {
        // 1. 해당 채팅방 엔티티 조회
        ChatRoom room = chatRoomRepository.findById(Long.parseLong(messageDto.getRoomId()))
                .orElseThrow(
//                        -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
                );

        // 2. DTO -> Entity 변환
        User dummyUser = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        System.out.println("nickname : " + dummyUser.getUserId());

        ChatMessage messageEntity = ChatMessage.builder()
                .chatRoom(room)
//                .senderId(1L) // 임시 ID (나중에 실제 로그인 유저 ID로 변경)
                .sender(dummyUser)
                .content(messageDto.getMessage())
                .msgType(messageDto.getType())
                .build();

        // 3. DB 저장
        chatMessageRepository.save(messageEntity);

        // 4. (선택) 채팅방의 마지막 메시지 정보 업데이트
        room.updateLastMessage(messageDto.getMessage());
    }

    /**
     * 채팅방의 이전 메시지 내역 조회
     */
    public List<ChatMessageDto> getChatMessages(Long roomId) {
        List<ChatMessage> entities = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(roomId);

        // Entity 리스트를 Dto 리스트로 변환
        return entities.stream()
                .map(entity -> ChatMessageDto.builder()
                        .type(entity.getMsgType())
                        .roomId(entity.getChatRoom().getId().toString())
                        .sender(entity.getSender().getNickname()) // 임시 닉네임
                        .message(entity.getContent())
                        .build())
                .collect(Collectors.toList());
    }

}
