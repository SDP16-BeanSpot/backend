package com.beanspot.backend.dto.announcement;

import com.beanspot.backend.entity.announcement.AnnouncementDocument;
import com.beanspot.backend.entity.announcement.Announcement;
import com.beanspot.backend.entity.announcement.AnnouncementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
public class AnnouncementSummaryDTO {
    private Long id;
    private String title;
    private String region;
    private String activityMethod;
    private AnnouncementType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate recruitmentEnd;
    private String thumbnailUrl;

    private boolean bookmarked;

    public static AnnouncementSummaryDTO from(Announcement announcement, boolean bookmarked) {
        return AnnouncementSummaryDTO.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .region(announcement.getRegion())
                .activityMethod(announcement.getActivityMethod())
                .type(announcement.getType())
                .startDate(announcement.getStartDate())
                .endDate(announcement.getEndDate())
                .recruitmentEnd(announcement.getRecruitmentEnd())
                .bookmarked(bookmarked)
                .build();
    }

    public static AnnouncementSummaryDTO from(AnnouncementDocument doc, boolean bookmarked) {
        return AnnouncementSummaryDTO.builder()
                .id(doc.getId())
                .title(doc.getTitle())
                .region(doc.getRegion())
                .activityMethod(doc.getActivityMethod())
                .type(AnnouncementType.valueOf(doc.getType()))
                .startDate(doc.getStartDate())
                .endDate(doc.getEndDate())
                .recruitmentEnd(doc.getRecruitmentEnd())
                .thumbnailUrl(doc.getThumbnailUrl())
                .bookmarked(bookmarked)
                .build();
    }
}
