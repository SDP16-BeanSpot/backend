package com.beanspot.backend.controller;

import com.beanspot.backend.common.response.ApiResponse;
import com.beanspot.backend.common.response.PageResponse;
import com.beanspot.backend.dto.report.AdminReportDetailResponse;
import com.beanspot.backend.dto.report.AdminReportListResponse;
import com.beanspot.backend.dto.report.AdminReportUpdateRequest;
import com.beanspot.backend.entity.ReportStatus;
import com.beanspot.backend.service.AdminReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 신고 API", description = "신고 목록 조회 및 처리 API입니다. ROLE_ADMIN만 접근 가능합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminReportController {

    private final AdminReportService adminReportService;

    @Operation(summary = "신고 목록 조회", description = "신고 목록을 조회합니다. status 파라미터로 필터링 가능 (PENDING/COMPLETED/REJECTED). 페이지당 20개 고정, 최신순 정렬.")
    @GetMapping("/reports")
    public ApiResponse<PageResponse<AdminReportListResponse>> getReports(
            @RequestParam(required = false) ReportStatus status,
            @RequestParam(defaultValue = "0") int page) {
        return ApiResponse.ok(adminReportService.getReports(status, PageRequest.of(page, 20)));
    }

    @Operation(summary = "신고 상세 조회", description = "신고 상세 정보를 조회합니다.")
    @GetMapping("/reports/{reportId}")
    public ApiResponse<AdminReportDetailResponse> getReport(@PathVariable Long reportId) {
        return ApiResponse.ok(adminReportService.getReport(reportId));
    }

    @Operation(summary = "신고 처리", description = "신고를 처리합니다. status: COMPLETED(처리 완료) 또는 REJECTED(반려)")
    @PatchMapping("/reports/{reportId}")
    public ApiResponse<AdminReportDetailResponse> processReport(
            @PathVariable Long reportId,
            @Valid @RequestBody AdminReportUpdateRequest request) {
        return ApiResponse.ok(adminReportService.processReport(reportId, request.getStatus()));
    }
}
