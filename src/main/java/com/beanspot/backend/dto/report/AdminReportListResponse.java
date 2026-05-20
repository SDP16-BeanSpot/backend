package com.beanspot.backend.dto.report;

import com.beanspot.backend.entity.Report;
import com.beanspot.backend.entity.ReportStatus;
import com.beanspot.backend.entity.ReportType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminReportListResponse {

    private Long reportId;
    private String reporterNickname;
    private String reportedUserNickname;
    private ReportType reportType;
    private ReportStatus status;
    private LocalDateTime createdAt;

    public static AdminReportListResponse from(Report report) {
        return AdminReportListResponse.builder()
                .reportId(report.getId())
                .reporterNickname(report.getReporter().getNickname())
                .reportedUserNickname(report.getReportedUser().getNickname())
                .reportType(report.getReportType())
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
