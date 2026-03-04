package com.beanspot.backend.controller;

import com.beanspot.backend.common.response.ApiResponse;
import com.beanspot.backend.dto.user.NotificationDTO;
import com.beanspot.backend.security.CurrentUserId;
import com.beanspot.backend.service.notification.NotificationService;
import com.beanspot.backend.service.notification.NotificationSettingService;
import com.beanspot.backend.service.notification.UserKeywordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name="알림 관련 API", description = "알림 목록 조회 및 설정 변경과 관련된 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationSettingService settingService;
    private final NotificationService notificationService;
    private final UserKeywordService keywordService;

    @GetMapping()
    @Operation(summary = "알림 내역 목록 조회", description = "유저가 받은 알림들을 최신순으로 가져옵니다.")
    public ApiResponse<?> getMyNotifications(@CurrentUserId Long userId) {
        return ApiResponse.ok(notificationService.getNotifications(userId));
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음 상태로 변경합니다.")
    public ApiResponse<Void> readNotification(@CurrentUserId Long userId, @PathVariable Long notificationId) {
        notificationService.markAsRead(userId, notificationId);
        return ApiResponse.ok(null);
    }


    @DeleteMapping("/{notificationId}")
    @Operation(summary = "알림 삭제", description = "특정 알림 내역을 삭제합니다.")
    public ApiResponse<Void> deleteNotification(@CurrentUserId Long userId, @PathVariable Long notificationId) {
        notificationService.deleteNotification(userId, notificationId);
        return ApiResponse.ok(null);
    }

    @GetMapping("/unread-count")
    @Operation(summary = "읽지 않은 알림 개수 조회", description = "사용자가 아직 읽지 않은 알림의 총 개수를 반환합니다.")
    public ApiResponse<Long> getUnreadNotificationCount(@CurrentUserId Long userId) {
        return ApiResponse.ok(notificationService.getUnreadNotificationCount(userId));
    }

    @GetMapping("/setting")
    @Operation(summary = "내 알림 설정 조회", description = "현재 로그인한 사용자의 모든 알림 설정 상태를 가져옵니다.")
    public ApiResponse<NotificationDTO.SettingResponse> getMySettings(@CurrentUserId Long userId) {
        return ApiResponse.ok(settingService.getSetting(userId));
    }

    @PatchMapping("/setting")
    @Operation(summary = "알림 설정 통합 수정", description = "카테고리 별 상세 옵션 수정을 처리하는 API입니다.")
    public ApiResponse<NotificationDTO.SettingResponse> updateSetting(@CurrentUserId Long userId, @RequestBody NotificationDTO.UpdateSettingRequest request) {
        return ApiResponse.ok(settingService.updateNotificationSetting(userId, request));
    }

    @GetMapping("/setting/keyword")
    @Operation(summary = "내 키워드 목록 조회", description = "등록한 키워드 리스트를 최신순으로 가져옵니다.")
    public ApiResponse<List<NotificationDTO.KeywordResponse>> getKeywords(@CurrentUserId Long userId) {
        return ApiResponse.ok(keywordService.getMyKeywords(userId));
    }

    @PostMapping("/setting/keyword")
    @Operation(summary = "키워드 등록", description = "새로운 알림 키워드를 등록합니다. (최대 30개)")
    public ApiResponse<NotificationDTO.KeywordResponse> addKeyword(
            @CurrentUserId Long userId,
            @RequestBody NotificationDTO.KeywordRequest request) {
        return ApiResponse.ok(keywordService.addKeyword(userId, request.getKeyword()));
    }

    @DeleteMapping("/setting/keyword/{keywordId}")
    @Operation(summary = "키워드 삭제", description = "특정 키워드를 삭제합니다.")
    public ApiResponse<String> removeKeyword(@CurrentUserId Long userId, @PathVariable Long keywordId) {
        keywordService.deleteKeyword(userId, keywordId);
        return ApiResponse.ok("삭제 완료");
    }
}
