package com.beanspot.backend.service.notification;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.common.util.FcmUtil;
import com.beanspot.backend.dto.user.NotificationDTO;
import com.beanspot.backend.entity.Bookmark;
import com.beanspot.backend.entity.User;
import com.beanspot.backend.entity.announcement.Announcement;
import com.beanspot.backend.entity.notification.*;
import com.beanspot.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationHistoryRepository historyRepository;
    private final BookmarkRepository bookmarkRepository;
    private final NotificationSettingRepository settingRepository;
    private final UserKeywordRepository keywordRepository;
    private final FcmUtil fcmUtil;
    private final UserDeviceRepository deviceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO.HistoryResponse> getNotifications(Long userId) {

        List<NotificationHistory> notifications =  historyRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        return notifications.stream()
                .map(NotificationDTO.HistoryResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public void markAsRead(Long userId, Long notificationId) {
        NotificationHistory history = historyRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_HISTORY_NOT_FOUND));

        validateOwnership(userId, history);

        history.markAsRead();

    }

    @Override
    @Transactional
    public void deleteNotification(Long userId, Long notificationId) {
        NotificationHistory history = historyRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_HISTORY_NOT_FOUND));

        validateOwnership(userId, history);

        historyRepository.delete(history);

    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadNotificationCount(Long userId) {
        return historyRepository.countByUserIdAndIsReadFalse(userId);

    }

    @Override
    @Transactional
    public void sendKeywordNotification(Announcement announcement) {
        List<UserKeyword> keywords = keywordRepository.findAll();

        for(UserKeyword uk : keywords) {
            // 해당 유저가 키워드 알림을 켜두었는지 확인
            NotificationSetting setting = settingRepository.findByUserId(uk.getUser().getId()).orElse(null);
            if (setting == null || !setting.isKeywordEnabled()) continue;

            // 공고 제목에 키워드가 포함되어 있다면 알림 생성
            if (announcement.getTitle().contains(uk.getKeyword())) {
                createHistory(uk.getUser(),
                        "키워드 알림",
                        "등록하신 키워드 '" + uk.getKeyword() + "'(이)가 포함된 새로운 공고가 올라왔어요!",
                        NotificationType.KEYWORD);
            }
        }
    }

    @Override
    @Transactional
    public void sendDeadlineNotification(Announcement announcement, String optionType) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByAnnouncement(announcement);

        for (Bookmark bookmark : bookmarks) {
            User user = bookmark.getUser();
            NotificationSetting setting = settingRepository.findByUserId(user.getId()).orElse(null);

            if (setting != null && setting.isDeadlineEnabled() && isOptionEnabled(setting, optionType)) {
                String content = String.format("[%s] 공고 마감이 %s 전입니다!",
                        announcement.getTitle(),
                        optionType.replace("D", "D-").replace("DAY", "Day"));

                createHistory(user, "마감 임박 알림", content, NotificationType.DEADLINE);
                sendPushNotification(user, "마감 임박 알림", content, NotificationType.DEADLINE, announcement.getId());
            }

        }
    }

    private boolean isOptionEnabled(NotificationSetting setting, String optionType) {
        return switch (optionType){
            case "D7" -> setting.isDeadlineD7();
            case "D3" -> setting.isDeadlineD3();
            case "D1" -> setting.isDeadlineD1();
            case "DAY" -> setting.isDeadlineDay();
            default -> false;
        };
    }

    private void createHistory(User user, String title, String content, NotificationType type) {
        NotificationHistory history = NotificationHistory.builder()
                .user(user)
                .title(title)
                .content(content)
                .type(type)
                .isRead(false)
                .build();
        historyRepository.save(history);

    }

    public void sendPushNotification(User user, String title, String content, NotificationType type, Long announcementId) {
        NotificationSetting setting = settingRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_SETTING_NOT_FOUND));

        if(!isNotificationEnabled(setting, type)) {
            log.info("유저 {}가 {} 알림을 비활성화하여 발송을 건너뜁니다.", user.getId(), type);
            return;
        }

        List<UserDevice> devices = deviceRepository.findAllByUserId(user.getId());
        for (UserDevice device : devices) {
            fcmUtil.sendNotification(device.getFcmToken(), title, content, String.valueOf(announcementId));
        }
    }

    private void validateOwnership(Long userId, NotificationHistory history) {
        if(!history.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

    private boolean isNotificationEnabled(NotificationSetting setting, NotificationType type) {
        return switch (type) {
            case BOOKMARK -> setting.isBookmarkEnabled();
            case DEADLINE -> setting.isDeadlineEnabled();
            case KEYWORD -> setting.isKeywordEnabled();
            case TODO -> setting.isTodoEnabled();
            default -> false;
        };
    }
}
