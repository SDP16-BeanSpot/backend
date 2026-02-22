package com.beanspot.backend.entity.announcement;
import com.beanspot.backend.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private AnnouncementType type;  //공고 유형

    private String title;           //공고 제목

    @Column(name = "img_url")
    private String imgUrl;      //공고 포스터

    private String organizer;       //운영 주체

    @Column(name = "organizer_img_url")
    private String organizerImgUrl; // 운영 주체 사진

    @Column(name = "activity_content", columnDefinition = "TEXT")
    private String activityContent;  // 활동 내용

    private String target;           // 공고 대상

    private String recruitmentCount; // 모집 인원

    private String applyMethod;      // 접수 방법 (온라인 접수/ 이메일 접수)

    @Column(name = "link_url")
    private String linkUrl;     //신청 링크

    @Column(nullable = false)
    private String location;        //활동 장소

    @Column(name = "start_date")
    private LocalDate startDate;    //활동 시작일

    @Column(name = "end_date")
    private LocalDate endDate;      //활동 종료일

    private String activityMethod;   // 활동 방식 (온/오프라인)


    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sequenceOrder ASC")
    private List<AnnouncementSchedule> schedules = new ArrayList<>();

    @Column(name = "recruitment_start")
    private LocalDate recruitmentStart;    //접수 시작일

    @Column(name = "recruitment_end")
    private LocalDate recruitmentEnd;       //접수 마감일

    private Integer fee;            //참가비 or 비용

    @Column(name = "detail_content", columnDefinition = "TEXT")
    private String detailContent; // 상세 내용

    private String benefits;         // 활동 혜택

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


    @Column(name = "view_count")
    private int viewCount;      //조회수

    //지도용 위경도
    private Double lat;
    private Double lng;

    @Column(length = 20, nullable = false)
    private String region;          //행정구

}
