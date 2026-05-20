package com.beanspot.backend.dto.report;

import com.beanspot.backend.entity.Report;
import com.beanspot.backend.entity.ReportStatus;
import com.beanspot.backend.entity.ReportType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminReportDetailResponse {

    private Long reportId;
    private String reporterNickname;
    private String reportedUserNickname;
    private ReportType reportType;
    private String content;
    private String messageContent;
    private ReportStatus status;
    private LocalDateTime createdAt;

    public static AdminReportDetailResponse from(Report report) {
        return AdminReportDetailResponse.builder()
                .reportId(report.getId())
                .reporterNickname(report.getReporter().getNickname())
                .reportedUserNickname(report.getReportedUser().getNickname())
                .reportType(report.getReportType())
                .content(report.getContent())
                .messageContent(report.getMessage().getContent())
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
