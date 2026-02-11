package com.beanspot.backend.controller;

import com.beanspot.backend.common.response.ApiResponse;
import com.beanspot.backend.dto.announcement.AnnouncementDTO;
import com.beanspot.backend.service.AnnouncementIndexService;
import com.beanspot.backend.service.announcement.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Tag(name="관리자용 공고 관련 API", description = "관리자용 공고 관련 API입니다.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/announcement")
public class AdminAnnouncementController {

    private final AnnouncementService announcementService;
    private final AnnouncementIndexService indexService;
    @Operation(
            summary = "공고 등록"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "공고 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값이 올바르지 않음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한 없음")
    })
    @PostMapping()
    public ApiResponse<String> createAnnouncement(@RequestBody AnnouncementDTO.Create request) {
        announcementService.addAnnouncement(request);
        return ApiResponse.ok("공고 등록");
    }
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "공고 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값이 올바르지 않음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "공고를 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ApiResponse<String> update(@PathVariable Long id, @RequestBody AnnouncementDTO.Update request) {
        announcementService.updateAnnouncement(id, request);
        return ApiResponse.ok("공고 업데이트");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(
            @Parameter(description = "공고 ID", example = "1", required = true)
            @PathVariable
            Long id
    ){
        announcementService.deleteAnnouncement(id);
        return ApiResponse.ok("공고 삭제");
    }



    // 전체 공고 ES 재색인
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "재색인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 필요"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한 없음")
    })
    @PostMapping("/es/reindex")
    public Map<String, Object> reindexAll() {
        int count = indexService.reindexAll();

        return Map.of("message", "ES reindex completed","indexedCount", count);
    }


}
