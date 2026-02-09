package com.beanspot.backend.entity.announcement;
import com.beanspot.backend.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "announcement_common")
public class Announcement extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 공고 유형
    @Enumerated(EnumType.STRING)
    private AnnouncementType type;  //공고 유형

    private String title;           //공고 제목

    @Column(columnDefinition = "TEXT")
    private String content;         //상세 내용

    private String organizer;       //운영 주체

    private Integer fee;            //참가비 or 비용

    @Column(name = "img_url")
    private String imgUrl;      //공고 포스터

    @Column(name = "link_url")
    private String linkUrl;     //신청 링크

    @Column(nullable = false)
    private String location;        //활동 장소

    @Column(length = 20, nullable = false)
    private String region;          //행정구

    @Column(name = "start_date")
    private LocalDate startDate;    //활동 시작일

    @Column(name = "end_date")
    private LocalDate endDate;      //활동 종료일

    @Column(columnDefinition = "TEXT") // 추후 확장시 별도 테이블로 분리
    private String scheduleDetail;      //세부 일정

    @Column(name = "recruitment_start")
    private LocalDate recruitmentStart;    //모집 시작일

    @Column(name = "recruitment_end")
    private LocalDate recruitmentEnd;       //모집 마감일

    @Column(name = "view_count")
    private int viewCount;      //조회수

    //지도용 위경도
    private Double lat;
    private Double lng;

    // volunteer
    @Column(name = "service_hours_verified")
    private String serviceHoursVerified;       //봉사시간 인정 여부 -> null이면 미인정
    //Supporter

    @Column(name = "selection_process",columnDefinition = "TEXT")
    private String selectionProcess;        //심사 방식
    @Column(name = "award_scale", columnDefinition = "TEXT")
    private String awardScale;      //시상 규모

    @Column(name = "team_size")
    private String teamSize;        //팀원 규모

}
