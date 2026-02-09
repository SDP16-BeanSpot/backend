package com.beanspot.backend.dto.announcement;

import com.beanspot.backend.entity.announcement.Announcement;
import com.beanspot.backend.entity.announcement.AnnouncementType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class AnnouncementDTO {

    @Builder
    @Getter
    @Setter
    public static class Detail{
        private Long id;
        private String title;
        private String content;
        private String organizer;
        private AnnouncementType type;
        private String imgUrl;

        private String region;
        private String location;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate recruitmentStart;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate recruitmentEnd;

        private Integer fee;
        private int viewCount;
        private String scheduleDetail;
        private String linkUrl;
        private String serviceHoursVerified;
        private String selectionProcess;
        private String teamSize;
        private String awardScale;

        public static Detail from(Announcement announcement) {
            return Detail.builder()
                    .id(announcement.getId())
                    .title(announcement.getTitle())
                    .content(announcement.getContent())
                    .organizer(announcement.getOrganizer())
                    .type(announcement.getType())
                    .imgUrl(announcement.getImgUrl())
                    .region(announcement.getRegion())
                    .location(announcement.getLocation())
                    .startDate(announcement.getStartDate())
                    .endDate(announcement.getEndDate())
                    .recruitmentStart(announcement.getRecruitmentStart())
                    .recruitmentEnd(announcement.getRecruitmentEnd())
                    .fee(announcement.getFee())
                    .viewCount(announcement.getViewCount())
                    .scheduleDetail(announcement.getScheduleDetail())
                    .linkUrl(announcement.getLinkUrl())
                    .serviceHoursVerified(announcement.getServiceHoursVerified())
                    .selectionProcess(announcement.getSelectionProcess())
                    .teamSize(announcement.getTeamSize())
                    .awardScale(announcement.getAwardScale())
                    .build();
        }
    }

    @Getter
    public static class Create{
        @NotBlank
        private String title;
        @NotBlank
        private String content;
        @NotBlank
        private AnnouncementType type;

        @NotBlank
        private String organizer;

        @NotBlank
        private String imgUrl;

        @NotBlank
        private String region;

        @NotBlank
        private String location;


        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate recruitmentStart;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate recruitmentEnd;

        private Integer fee;

        //유형별 (선택)

        // VOLUNTEER
        private String serviceHoursVerified;

        // SUPPORTER
        private String selectionProcess;

        // CHALLENGE
        private String teamSize;
        private String awardScale;

        // 기타

        private String scheduleDetail;

    }

    @Getter
    public static class Update{
        private String title;
        private String content;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;

    }
}
