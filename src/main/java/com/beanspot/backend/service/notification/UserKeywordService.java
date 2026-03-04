package com.beanspot.backend.service.notification;

import com.beanspot.backend.dto.user.NotificationDTO;

import java.util.List;

public interface UserKeywordService {
    List<NotificationDTO.KeywordResponse> getMyKeywords(Long userId);
    NotificationDTO.KeywordResponse addKeyword(Long userId, String keyword);
    void deleteKeyword(Long userId, Long keywordId);
}
