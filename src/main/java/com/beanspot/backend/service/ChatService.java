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
    public void saveMessage(ChatMessageDto messageDto, String userId) {// userId 추가
        // 1. 넘겨받은 ID(인터셉터에서 추출한 진짜 ID)로 DB에서 유저 찾기
        User sender = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 2. 해당 채팅방 엔티티 조회 (기존 로직 유지)
        ChatRoom room = chatRoomRepository.findById(Long.parseLong(messageDto.getRoomId()))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

        // 3. Entity 빌드 (더미 유저 삭제!)
        ChatMessage messageEntity = ChatMessage.builder()
                .chatRoom(room)
                .sender(sender) // 이제 진짜 유저인 sender를 넣습니다!
                .content(messageDto.getMessage())
                .msgType(messageDto.getType())
                .build();

        // 4. DB 저장
        chatMessageRepository.save(messageEntity);

        // 5. 채팅방의 마지막 메시지 업데이트
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

    public Slice<ChatMessage> getChatMessages(Long roomId, int page, int size) {
        // 최신순으로 정렬해서 요청한 페이지만큼 가져오기
        Pageable pageable = PageRequest.of(page, size);
        return chatMessageRepository.findByChatRoomIdOrderByCreatedAtDesc(roomId, pageable);
    }
}
