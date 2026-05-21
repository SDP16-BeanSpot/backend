package com.beanspot.backend.controller;

import com.beanspot.backend.common.response.ApiResponse;
import com.beanspot.backend.dto.report.ReportRequest;
import com.beanspot.backend.dto.report.ReportResponse;
import com.beanspot.backend.security.CurrentUserId;
import com.beanspot.backend.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "신고 API", description = "채팅 메시지 신고 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/messages")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "메시지 신고", description = "특정 메시지를 신고합니다. 동일 메시지 중복 신고 불가.")
    @PostMapping("/{messageId}/report")
    public ApiResponse<ReportResponse> report(
            @PathVariable Long messageId,
            @Valid @RequestBody ReportRequest request,
            @CurrentUserId Long userId) {
        return ApiResponse.created(reportService.report(userId, messageId, request));
    }
}
