package com.beanspot.backend.dto.announcement;

import com.beanspot.backend.entity.announcement.AnnouncementDocument;
import com.beanspot.backend.entity.announcement.Announcement;
import com.beanspot.backend.entity.announcement.AnnouncementType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class AnnouncementSummaryDTO {
    private Long id;
    private String title;
    private String region;
    private AnnouncementType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String thumbnailUrl;

    public static AnnouncementSummaryDTO from(Announcement announcement) {
        return AnnouncementSummaryDTO.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .region(announcement.getRegion())
                .type(announcement.getType())
                .startDate(announcement.getStartDate())
                .endDate(announcement.getEndDate())
                .build();
    }

    public static AnnouncementSummaryDTO from(AnnouncementDocument doc) {
        return AnnouncementSummaryDTO.builder()
                .id(doc.getId())
                .title(doc.getTitle())
                .region(doc.getRegion())
                .type(AnnouncementType.valueOf(doc.getType()))
                .startDate(doc.getStartDate())
                .endDate(doc.getEndDate())
                .thumbnailUrl(doc.getThumbnailUrl())
                .build();
    }
}
