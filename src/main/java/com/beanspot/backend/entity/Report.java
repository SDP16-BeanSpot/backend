package com.beanspot.backend.entity;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
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

    private String content;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private LocalDateTime createdAt;

    @Builder
    public Report(User reporter, User reportedUser, ChatMessage message, ReportType reportType, String content) {
        this.reporter = reporter;
        this.reportedUser = reportedUser;
        this.message = message;
        this.reportType = reportType;
        this.content = content;
        this.status = ReportStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void complete() {
        if (this.status != ReportStatus.PENDING) {
            throw new CustomException(ErrorCode.REPORT_ALREADY_PROCESSED);
        }
        this.status = ReportStatus.COMPLETED;
    }

    public void reject() {
        if (this.status != ReportStatus.PENDING) {
            throw new CustomException(ErrorCode.REPORT_ALREADY_PROCESSED);
        }
        this.status = ReportStatus.REJECTED;
    }
}
