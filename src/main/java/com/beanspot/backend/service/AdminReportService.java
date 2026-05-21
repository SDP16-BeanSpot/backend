package com.beanspot.backend.service;

import com.beanspot.backend.common.exception.CustomException;
import com.beanspot.backend.common.exception.ErrorCode;
import com.beanspot.backend.common.response.PageResponse;
import com.beanspot.backend.dto.report.AdminReportDetailResponse;
import com.beanspot.backend.dto.report.AdminReportListResponse;
import com.beanspot.backend.entity.Report;
import com.beanspot.backend.entity.ReportStatus;
import com.beanspot.backend.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportService {

    private final ReportRepository reportRepository;

    public PageResponse<AdminReportListResponse> getReports(ReportStatus status, Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<Report> reports = (status == null)
                ? reportRepository.findAllWithDetails(pageRequest)
                : reportRepository.findAllByStatusWithDetails(status, pageRequest);
        Page<AdminReportListResponse> mapped = reports.map(AdminReportListResponse::from);
        return PageResponse.of(mapped, mapped.getContent());
    }

    public AdminReportDetailResponse getReport(Long reportId) {
        Report report = reportRepository.findByIdWithDetails(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));
        return AdminReportDetailResponse.from(report);
    }

    @Transactional
    public AdminReportDetailResponse processReport(Long reportId, ReportStatus status) {
        Report report = reportRepository.findByIdWithDetails(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));
        if (status == ReportStatus.COMPLETED) {
            report.complete();
        } else if (status == ReportStatus.REJECTED) {
            report.reject();
        } else {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
        return AdminReportDetailResponse.from(report);
    }
}
