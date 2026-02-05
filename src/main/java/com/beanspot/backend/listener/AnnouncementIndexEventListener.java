package com.beanspot.backend.listener;

import com.beanspot.backend.repository.announcement.AnnouncementRepository;
import com.beanspot.backend.service.AnnouncementIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AnnouncementIndexEventListener {
    private final AnnouncementRepository announcementRepository;
    private final AnnouncementIndexService indexService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAnnouncementCreated(AnnouncementCreatedEvent event) {
        indexService.index(
                announcementRepository.findById(event.announcementId())
                        .orElseThrow()
        );
    }
}
