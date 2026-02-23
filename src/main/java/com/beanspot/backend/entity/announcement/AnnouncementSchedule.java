package com.beanspot.backend.entity.announcement;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "announcement_schedule")
public class AnnouncementSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id", nullable = false)
    private Announcement announcement;

    @Column(name = "schedule_date", nullable = false)
    private String scheduleDate; // 예: "01.11", "01.12" 등 날짜

    @Column(nullable = false)
    private String content;      // 예: "대면 발대식", "기자단 활동 진행"

    @Column(name = "sequence_order", nullable = false)
    private int sequenceOrder;
}
