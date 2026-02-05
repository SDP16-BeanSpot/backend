package com.beanspot.backend.entity.announcement;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.time.LocalDate;

@Document(indexName = "announcement", createIndex = true)
@Setting(settingPath = "elasticsearch/announcement-setting.json")
@Mapping(mappingPath = "elasticsearch/announcement-mapping.json")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    //추후 트래픽 많아지면 제거
    @Field(type = FieldType.Text)
    private String content;

    //필터

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Keyword)
    private String region;

    // 모집/활동 기간

    @Field(type = FieldType.Date)
    private LocalDate recruitmentStart;

    @Field(type = FieldType.Date)
    private LocalDate recruitmentEnd;


    @Field(type = FieldType.Date)
    private LocalDate startDate;

    @Field(type = FieldType.Date)
    private LocalDate endDate;

    //지도
    @GeoPointField
    private GeoPoint geoLocation;

    // 리스트 표시용

    @Field(type = FieldType.Keyword)
    private String thumbnailUrl;

    //정렬

    @Field(type = FieldType.Long)
    private Long viewCount;

    @Field(type = FieldType.Date)
    private String createdAt;

    // Entity → Document 변환
    public static AnnouncementDocument of(Announcement announcement) {

        GeoPoint geoPoint = null;
        if (announcement.getLat() != null && announcement.getLng() != null) {
            geoPoint = new GeoPoint(
                    announcement.getLat(),
                    announcement.getLng()
            );
        }

        return  AnnouncementDocument.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .type(announcement.getType().name())
                .region(announcement.getRegion())
                .recruitmentStart(announcement.getRecruitmentStart())
                .recruitmentEnd(announcement.getRecruitmentEnd())
                .startDate(announcement.getStartDate())
                .endDate(announcement.getEndDate())
                .thumbnailUrl(announcement.getImgUrl())
                .viewCount((long) announcement.getViewCount())
                .createdAt(
                        announcement.getCreatedAt() != null
                                ? announcement.getCreatedAt().toString()
                                : null
                )
                .geoLocation(geoPoint)
                .build();
    }

}
