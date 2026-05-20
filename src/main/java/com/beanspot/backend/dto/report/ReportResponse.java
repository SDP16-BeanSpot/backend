package com.beanspot.backend.dto.report;

import com.beanspot.backend.entity.Report;
import com.beanspot.backend.entity.ReportStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportResponse {

    private Long reportId;
    private ReportStatus status;

    public static ReportResponse from(Report report) {
        return ReportResponse.builder()
                .reportId(report.getId())
                .status(report.getStatus())
                .build();
    }
}
