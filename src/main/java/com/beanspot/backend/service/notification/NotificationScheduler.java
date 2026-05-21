package com.beanspot.backend.service.notification;

import com.beanspot.backend.entity.announcement.Announcement;
import com.beanspot.backend.repository.announcement.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {

    private final AnnouncementRepository announcementRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    public void sendDeadlineNotifications() {
        log.info("마감 임박 공고 알림 스케줄러 실행 시작");
        LocalDate today = LocalDate.now();

        // 1. D-7, D-3, D-1, D-Day에 해당하는 공고들 조회
        checkAndSend(today.plusDays(7), "D7");
        checkAndSend(today.plusDays(3), "D3");
        checkAndSend(today.plusDays(1), "D1");
        checkAndSend(today, "DAY");

        log.info("마감 임박 공고 알림 스케줄러 실행 완료");
    }

    private void checkAndSend(LocalDate targetDate, String optionType) {

        List<Announcement> announcements = announcementRepository.findAllByRecruitmentEnd(targetDate);

        for (Announcement announcement : announcements) {
            notificationService.sendDeadlineNotification(announcement, optionType);
        }
    }
}
