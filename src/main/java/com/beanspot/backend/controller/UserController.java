package com.beanspot.backend.controller;

import com.beanspot.backend.common.response.ApiResponse;
import com.beanspot.backend.dto.user.UserDTO;
import com.beanspot.backend.security.CurrentUserId;
import com.beanspot.backend.service.MypageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Tag(name="마이페이지관련 관련 API", description = "마이페이지 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final MypageService mypageService;

    @GetMapping("/profile")
    @Operation(summary = "계정 정보 조회", description = "사용자의 휴대폰 번호, 이름 등 계정 정보를 조회합니다.")
    public ApiResponse<?> getProfile(@CurrentUserId Long userId) {
        UserDTO.ProfileResponse user = mypageService.getAccountInfo(userId);
         return ApiResponse.ok(user);
    }

    @PatchMapping("/profile")
    @Operation(summary = "프로필 수정", description = "사용자의 닉네임과 프로필 이미지를 변경합니다.")
    public ApiResponse<UserDTO.ProfileResponse> updateProfile(
            @CurrentUserId Long userId,
            @RequestPart(value = "nickname") String nickname,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        UserDTO.ProfileResponse response = mypageService.updateProfile(userId, nickname, image);
        return ApiResponse.ok(response);
    }

    @PatchMapping("/account")
    @Operation(summary = "계정 정보 수정", description = "사용자의 휴대폰 번호를 업데이트합니다.")
    public ApiResponse<?> updateAccount(
            @CurrentUserId Long userId,
            @Valid @RequestBody UserDTO.UpdateAccountRequest request
    ) {
        UserDTO.ProfileResponse response = mypageService.updateAccount(userId, request);
        return ApiResponse.ok("계정 정보 변경은 아직 구현되지 않았습니다.");
    }

    @DeleteMapping("/account")
    @Operation(summary = "회원 탈퇴", description = "사용자 계정을 영구 삭제하거나 비활성화합니다.")
    public ApiResponse<?> withdraw(@CurrentUserId Long userId) {
        mypageService.withdraw(userId);
        return ApiResponse.ok("탈퇴 완료");
    }

    // 관심 공고 리스트 조회
    @GetMapping("/bookmark")
    @Operation(summary = "내 관심 공고 리스트 조회", description = "내가 관심 공고 목록을 최신순으로 가져옵니다.")
    public ApiResponse<?> getBookmark(@CurrentUserId Long userId) {
        return ApiResponse.ok(mypageService.getMyBookmarks(userId));
    }

    //관심 공고 토글(등록/해제)
    @PostMapping("/bookmark/{announcementId}")
    @Operation(summary = "관심공고 토글", description = "공고를 찜하거나 찜 해제합니다. (결과가 true면 등록, false면 해제)")
    public ApiResponse<?> toggleBookmark(@CurrentUserId Long userId, @PathVariable Long announcementId) {
        return ApiResponse.ok(mypageService.toggleBookmark(userId, announcementId));
    }

    @GetMapping("/recent-view")
    @Operation(summary = "최근 본 공고 목록 조회", description = "사용자가 최근에 조회한 공고 20개를 최신순으로 반환합니다.")
    public ApiResponse<?> getRecentAnnouncements(@CurrentUserId Long userId) {
        return ApiResponse.ok(mypageService.getRecentAnnouncementViews(userId));
    }
}
