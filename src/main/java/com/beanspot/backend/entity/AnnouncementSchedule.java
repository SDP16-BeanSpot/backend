package com.beanspot.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter // 연관관계 편의 메서드에서 사용
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnnouncementSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id")
    private Announcement announcement; // 어떤 공고의 일정인지

    private String dateInfo;    // 날짜 (예: "01.11")
    private String description; // 내용 (예: "대면 발대식 진행")
}