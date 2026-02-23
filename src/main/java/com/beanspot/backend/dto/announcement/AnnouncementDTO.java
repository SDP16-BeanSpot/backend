package com.beanspot.backend.dto.announcement;

import com.beanspot.backend.entity.announcement.Announcement;
import com.beanspot.backend.entity.announcement.AnnouncementType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class AnnouncementDTO {

    @Builder
    @Getter
    @Setter
    public static class Detail{
        private Long id;
        private String title;
        private String content;
        private String organizer;
        private String organizerImgUrl;
        private String target;
        private String recruitmentCount;
        private String activityMethod;
        private String applyMethod;
        private String benefits;
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
        private String linkUrl;
        private String serviceHoursVerified;
        private String selectionProcess;
        private String teamSize;
        private String awardScale;

        private List<ScheduleResponseDTO> schedules;

        private String activityContent;
        private String detailContent;

        public static Detail from(Announcement announcement) {

            // 활동내용과 상세내용을 조합하여 하나의 본문 생성
            String combinedContent = String.format("%s\n\n%s",
                    announcement.getActivityContent() != null ? announcement.getActivityContent() : "",
                    announcement.getDetailContent() != null ? announcement.getDetailContent() : ""
            ).trim();

            return Detail.builder()
                    .id(announcement.getId())
                    .title(announcement.getTitle())
                    .content(combinedContent)
                    .organizer(announcement.getOrganizer())
                    .organizerImgUrl(announcement.getOrganizerImgUrl())
                    .type(announcement.getType())
                    .imgUrl(announcement.getImgUrl())
                    .target(announcement.getTarget())
                    .recruitmentCount(announcement.getRecruitmentCount())
                    .activityMethod(announcement.getActivityMethod())
                    .applyMethod(announcement.getApplyMethod())
                    .benefits(announcement.getBenefits())
                    .region(announcement.getRegion())
                    .location(announcement.getLocation())
                    .startDate(announcement.getStartDate())
                    .endDate(announcement.getEndDate())
                    .recruitmentStart(announcement.getRecruitmentStart())
                    .recruitmentEnd(announcement.getRecruitmentEnd())
                    .fee(announcement.getFee())
                    .viewCount(announcement.getViewCount())
                    .linkUrl(announcement.getLinkUrl())
                    .serviceHoursVerified(announcement.getServiceHoursVerified())
                    .selectionProcess(announcement.getSelectionProcess())
                    .teamSize(announcement.getTeamSize())
                    .awardScale(announcement.getAwardScale())
                    .activityContent(announcement.getActivityContent())
                    .detailContent(announcement.getDetailContent())
                    .schedules(announcement.getSchedules().stream()
                            .map(s -> ScheduleResponseDTO.builder()
                                    .scheduleDate(s.getScheduleDate())
                                    .content(s.getContent())
                                    .build())
                            .toList())
                    .build();

        }
    }@Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        // 1. 공고 기본 정보
        @NotBlank(message = "공고 제목은 필수입니다.")
        private String title;

        @NotBlank(message = "운영 주체명은 필수입니다.")
        private String organizer;

        private String organizerImgUrl; // 선택 (기본 이미지 제공 가능)

        @NotNull(message = "공고 유형은 필수입니다.")
        private AnnouncementType type;

        @NotBlank(message = "포스터 이미지 URL은 필수입니다.")
        private String imgUrl;

        @NotBlank(message = "활동 지역(구)은 필수입니다.")
        private String region; // 예: 강남구

        @NotBlank(message = "상세 활동 장소는 필수입니다.")
        private String location; // 예: 서울시 강남구...

        @NotBlank(message = "활동 방식은 필수입니다.")
        private String activityMethod; // 온라인, 오프라인

        @NotNull(message = "접수 시작일은 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate recruitmentStart;

        @NotNull(message = "접수 마감일은 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate recruitmentEnd;

        @NotNull(message = "활동 시작일은 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;

        @NotNull(message = "활동 종료일은 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;

        private String target;           // 공고 대상
        private String recruitmentCount; // 모집 인원
        private String applyMethod;      // 접수 방법 (홈페이지 지원 등)
        private String linkUrl;          // 신청 링크
        private Integer fee;             // 참가비 (null일 경우 무료 처리)
        private String benefits;         // 활동 혜택

        @NotBlank(message = "활동 내용은 필수입니다.")
        private String activityContent;

        @NotBlank(message = "상세 내용은 필수입니다.")
        private String detailContent;

        //  유형별 추가 정보
        private String serviceHoursVerified; // 봉사시간 인정 여부
        private String selectionProcess;    // 심사 방식
        private String teamSize;            // 팀원 규모
        private String awardScale;          //  시상 규모

        // 7. 세부 일정 리스트 (V10 테이블 분리 반영)
        private List<ScheduleRequestDTO> schedules;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ScheduleRequestDTO {
            @NotBlank
            private String scheduleDate; // 예: "01.11"
            @NotBlank
            private String content;      // 예: "대면 발대식"
        }
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
