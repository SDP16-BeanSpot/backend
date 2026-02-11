package com.beanspot.backend.dto.admin; // 패키지 경로 확인

import lombok.*;
import lombok.experimental.SuperBuilder; // 추가
import java.util.List;

@Getter
@SuperBuilder // @Builder 대신 필수
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementRequestDto {
    private String title;
    private String organizer;
    private String recruitmentCount;
    private String location;
    private String recruitmentStart;
    private String recruitmentEnd;
    private String startDate;
    private String endDate;
    private String type;
    private String imgUrl;
    private String linkUrl;
    private String content;
    private String closingment;

    // 상세 필드들
    private Boolean isServiceHoursVerified;
    private Integer fee;
    private String benefits;
    private String selectionProcess;
    private String prizeScale;
    private String participantTarget;
    private String teamSize;
    private String curriculumUrl;

    private List<ScheduleDto> schedules;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleDto {
        private String dateInfo;
        private String description;
    }
}