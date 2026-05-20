package com.beanspot.backend.entity;

import com.beanspot.backend.entity.chat.ChatMessage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private ChatMessage message;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private LocalDateTime createdAt;

    @Builder
    public Report(User reporter, User reportedUser, ChatMessage message, ReportType reportType) {
        this.reporter = reporter;
        this.reportedUser = reportedUser;
        this.message = message;
        this.reportType = reportType;
        this.status = ReportStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = ReportStatus.COMPLETED;
    }

    public void reject() {
        this.status = ReportStatus.REJECTED;
    }
}
