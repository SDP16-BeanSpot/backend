package com.beanspot.backend.service.notification;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.dto.user.NotificationDTO;
import com.beanspot.backend.entity.User;
import com.beanspot.backend.entity.notification.UserKeyword;
import com.beanspot.backend.repository.UserKeywordRepository;
import com.beanspot.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserKeywordServiceImpl implements UserKeywordService{

    private final UserKeywordRepository keywordRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO.KeywordResponse> getMyKeywords(Long userId) {
        return keywordRepository.findAllByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(NotificationDTO.KeywordResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public NotificationDTO.KeywordResponse addKeyword(Long userId, String keyword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_USERID_NOT_FOUND));

        if (keywordRepository.countByUserId(userId) >= 30) {
            throw new CustomException(ErrorCode.KEYWORD_LIMIT_EXCEEDED);
        }

        if (keywordRepository.existsByUserIdAndKeyword(userId, keyword)) {
            throw new CustomException(ErrorCode.DUPLICATE_KEYWORD);
        }

        UserKeyword userKeyword = UserKeyword.builder()
                .user(user)
                .keyword(keyword)
                .build();

        UserKeyword result = keywordRepository.save(userKeyword);

        return NotificationDTO.KeywordResponse.from(result);
    }

    @Override
    public void deleteKeyword(Long userId, Long keywordId) {
        UserKeyword userKeyword = keywordRepository.findById(keywordId)
                .orElseThrow(() -> new CustomException(ErrorCode.KEYWORD_NOT_FOUND));

        if (!userKeyword.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        keywordRepository.delete(userKeyword);
    }
}
