package com.beanspot.backend.controller;

import com.beanspot.backend.dto.admin.NoticeRequestDto;
import com.beanspot.backend.dto.admin.AnnouncementRequestDto;
import com.beanspot.backend.service.NoticeService;
import com.beanspot.backend.service.AdminReportService;
import com.beanspot.backend.security.CurrentUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // 추가
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

//    private final NoticeService noticeService;
//    private final AnnouncementService announcementService;
//    private final AdminReportService adminReportService;

    /**
     * 1. 공지사항 등록 API
     * POST /api/admin/notices
     */
//    @PostMapping("/notices")
//    public ResponseEntity<Long> createNotice(
//            @CurrentUserId Long adminId,
//            @RequestBody NoticeRequestDto dto) {
//
//        Long noticeId = noticeService.createNotice(adminId, dto);
//        // 생성(Post) 성공 시 201 Created 상태 코드를 권장합니다.
//        return ResponseEntity.status(HttpStatus.CREATED).body(noticeId);
//    }



    /**
     * 3. 신고 관리(Report) API
     * PATCH /api/admin/reports/{reportId}/complete
     */
//    @PatchMapping("/reports/{reportId}/complete")
//    public ResponseEntity<Map<String, Object>> completeReport(@PathVariable Long reportId) {
//        adminReportService.updateReportStatus(reportId);
//
//        // 메시지와 ID를 함께 반환하여 프론트엔드 처리를 돕습니다.
//        return ResponseEntity.ok(Map.of(
//                "message", "신고 처리가 완료되었습니다.",
//                "reportId", reportId
//        ));
//    }
}