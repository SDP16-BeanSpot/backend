package com.beanspot.backend.controller;

import com.beanspot.backend.dto.admin.NoticeRequestDto;
import com.beanspot.backend.dto.admin.AnnouncementRequestDto;
import com.beanspot.backend.service.NoticeService;
import com.beanspot.backend.service.AnnouncementService;
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
@PreAuthorize("hasRole('ADMIN')") // ⭐ 클래스 레벨에 추가: 관리자 권한이 없으면 이 컨트롤러 자체에 접근 불가
public class AdminController {

    private final NoticeService noticeService;
    private final AnnouncementService announcementService;
    private final AdminReportService adminReportService;

    /**
     * 1. 공지사항 등록 API
     * POST /api/admin/notices
     */
    @PostMapping("/notices")
    public ResponseEntity<Long> createNotice(
            @CurrentUserId Long adminId,
            @RequestBody NoticeRequestDto dto) {

        Long noticeId = noticeService.createNotice(adminId, dto);
        // 생성(Post) 성공 시 201 Created 상태 코드를 권장합니다.
        return ResponseEntity.status(HttpStatus.CREATED).body(noticeId);
    }

    /**
     * 2. 공고 관리(Announcement) API
     * POST /api/admin/announcements
     * 피그마 3단계(기본/상세/일정) 데이터를 한 번에 처리합니다.
     */
    @PostMapping("/announcements")
    public ResponseEntity<Long> createAnnouncement(
            @CurrentUserId Long adminId,
            @RequestBody AnnouncementRequestDto dto) {

        // 순서 주의: 서비스 정의에 따라 (dto, adminId)로 호출
        Long announcementId = announcementService.createAnnouncement(dto, adminId);
        return ResponseEntity.ok(announcementId);
    }

    /**
     * 3. 신고 관리(Report) API
     * PATCH /api/admin/reports/{reportId}/complete
     */
    @PatchMapping("/reports/{reportId}/complete")
    public ResponseEntity<Map<String, Object>> completeReport(@PathVariable Long reportId) {
        adminReportService.updateReportStatus(reportId);

        // 메시지와 ID를 함께 반환하여 프론트엔드 처리를 돕습니다.
        return ResponseEntity.ok(Map.of(
                "message", "신고 처리가 완료되었습니다.",
                "reportId", reportId
        ));
    }
}