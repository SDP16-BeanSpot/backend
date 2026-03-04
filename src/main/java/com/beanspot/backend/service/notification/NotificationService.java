package com.beanspot.backend.service.notification;

import com.beanspot.backend.dto.user.NotificationDTO;
import com.beanspot.backend.entity.announcement.Announcement;

import java.util.List;

public interface NotificationService {

    List<NotificationDTO.HistoryResponse> getNotifications(Long userId);

    void markAsRead(Long userId, Long notificationId);

    void deleteNotification(Long userId, Long notificationId);

    long getUnreadNotificationCount(Long userId);

    void sendKeywordNotification(Announcement announcement);

    void sendDeadlineNotification(Announcement announcement, String optionType);
}
