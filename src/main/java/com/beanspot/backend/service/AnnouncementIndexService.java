package com.beanspot.backend.service;

import com.beanspot.backend.entity.announcement.AnnouncementDocument;
import com.beanspot.backend.entity.announcement.Announcement;
import com.beanspot.backend.repository.announcement.AnnouncementRepository;
import com.beanspot.backend.repository.es.AnnouncementSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnouncementIndexService {

    private final AnnouncementSearchRepository searchRepository;
    private final AnnouncementRepository announcementRepository;

    // 단건 색인
    public void index(Announcement announcement) {
        searchRepository.save(AnnouncementDocument.of(announcement));
        log.info("[ES][INDEX] announcement indexed - id={}", announcement.getId());
    }

    // 전체 재색인 (RDB -> ES)
    @Transactional(readOnly = true)
    public int reindexAll(){
        log.info("[ES][REINDEX] start");

        List<Announcement> announcements = announcementRepository.findAll();
        if (announcements.isEmpty()) {
            log.info("[ES][REINDEX] no data in RDB");
            return 0;
        }

        List<AnnouncementDocument> docs = announcements.stream().map(AnnouncementDocument::of).toList();

        searchRepository.saveAll(docs);

        log.info("[ES][REINDEX] completed - count={}", docs.size());
        return docs.size();
    }

    // ES 문서 삭제
    public void delete(Long announcementId) {
        searchRepository.deleteById(announcementId);
        log.info("[ES][DELETE] announcement removed - id={}", announcementId);
    }

    // Bulk 색인
    public void bulkIndex(List<Announcement> announcements) {
        List<AnnouncementDocument> docs = announcements.stream()
                .map(AnnouncementDocument::of)
                .toList();

        searchRepository.saveAll(docs);
        log.info("[ES][BULK] bulk index completed - count={}", docs.size());
    }
}
