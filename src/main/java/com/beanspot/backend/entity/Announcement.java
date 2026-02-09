package com.beanspot.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter // 값 변경이 잦을 경우 대비
@SuperBuilder // 상속 관계에서 Builder를 사용하기 위해 필요
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED) // 조인 전략 선택
@DiscriminatorColumn(name = "type") // 부모 테이블에 자동 생성될 구분 컬럼명
@Table(name = "announcement")
public abstract class Announcement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announcement_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User admin;

    private String title;          // 공고 제목

    @Column(columnDefinition = "TEXT")
    private String content;        // 상세 내용

    private String organizer;      // 운영 주체
    private String recruitmentCount; // 모집 인원
    private String location;       // 활동 지역

    private LocalDateTime recruitmentStart; // 모집 시작일
    private LocalDateTime recruitmentEnd;   // 모집 종료일

    private LocalDateTime startDate; // 활동 시작일
    private LocalDateTime endDate;   // 활동 종료일

    private String imgUrl;         // 포스터 이미지 URL
    private String linkUrl;        // 신청 링크 URL
    private String closingment;    // 마무리 멘트

    @Builder.Default
    private Integer viewCount = 0; // 조회수

    @Builder.Default
    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnnouncementSchedule> schedules = new ArrayList<>();

    // == 연관관계 편의 메서드 == //
    public void addSchedule(AnnouncementSchedule schedule) {
        this.schedules.add(schedule);
        schedule.setAnnouncement(this);
    }
}