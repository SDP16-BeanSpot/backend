package com.beanspot.backend.controller;

import com.beanspot.backend.common.response.ApiResponse;
import com.beanspot.backend.common.response.PageResponse;
import com.beanspot.backend.dto.announcement.AnnouncementDTO;
import com.beanspot.backend.dto.announcement.AnnouncementSummaryDTO;
import com.beanspot.backend.dto.announcement.AnnouncementSearchConditionDTO;
import com.beanspot.backend.entity.announcement.AnnouncementType;
import com.beanspot.backend.entity.search.SortType;
import com.beanspot.backend.listener.SearchExcutedEvent;
import com.beanspot.backend.security.CurrentUserId;
import com.beanspot.backend.service.search.RecentSearchService;
import com.beanspot.backend.service.announcement.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@Tag(name="공고 관련 API", description = "공고 목록 조회 및 공고 상세 조회를 위한 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/announcement")
public class AnnouncementController {

    private final AnnouncementService announcementService;
    private final RecentSearchService recentSearchService;
    private final ApplicationEventPublisher applicationEventPublisher;
    @Operation(
            summary = "공고 목록 조회",
            description = """
            조건에 맞는 공고 목록을 조회합니다.
            -  keyword가 존재할 경우 최근 검색어로 저장됩니다.
            - 다양한 필터, 정렬, 페이징 조건을 지원합니다.
        """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값이 올바르지 않음")
    })
    @GetMapping("")
    public PageResponse<AnnouncementSummaryDTO> getAnnouncements(
            @Parameter(hidden = true)
            @CurrentUserId Long userId,
            @Parameter(
                    description = "검색 키워드",
                    example = "플로깅"
            )

            @RequestParam(required = false) String keyword,
            @Parameter(
                    description = "공고 유형",
                    example = "SUPPORT"
            )
            @RequestParam(required = false) AnnouncementType type,

            @RequestParam(required = false) List<String> hashtags,

            @Parameter(
                    description = "지역",
                    example = "강남구"
            )
            @RequestParam(required = false) String region,
            @Parameter(
                    description = "모집 기간 (yyyy-MM)",
                    example = "2026-03"
            )
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth recruitmentMonth,

            @Parameter(
                    description = "활동 기간 (yyyy-MM)",
                    example = "2026-04"
            )
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth activityMonth,

            @Parameter(description = "최소 위도", example = "37.4")
            @RequestParam(required = false) Double minLat,

            @Parameter(description = "최대 위도", example = "37.7")
            @RequestParam(required = false) Double maxLat,

            @Parameter(description = "최소 경도", example = "126.8")
            @RequestParam(required = false) Double minLng,

            @Parameter(description = "최대 경도", example = "127.1")
            @RequestParam(required = false) Double maxLng,

            @Parameter(description = "정렬 기준",example = "LATEST")
            @RequestParam(required = false, defaultValue = "LATEST") SortType sort,

            @Parameter(description = "최대 조회 개수", example = "20")
            @RequestParam(required = false) Integer limit,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        AnnouncementSearchConditionDTO condition =  AnnouncementSearchConditionDTO.builder()
                .keyword(keyword)
                .type(type)
                .hashtags(hashtags)
                .region(region)
                .recruitmentMonth(recruitmentMonth)
                .activityMonth(activityMonth)
                .minLat(minLat)
                .maxLat(maxLat)
                .minLng(minLng)
                .maxLng(maxLng)
                .sort(sort)
                .limit(limit)
                .page(page)
                .size(size)
                .build();
        //최근 검색어 저장
        String normalizedKeyword = keyword == null ? null : keyword.trim();
        if(userId != null && StringUtils.hasText(normalizedKeyword)) {
            recentSearchService.saveRecentSearch(userId, keyword);
            applicationEventPublisher.publishEvent(
                    new SearchExcutedEvent(userId,keyword)
            );
        }

        return announcementService.getAnnouncements(condition);
    }
    @Operation(
            summary = "공고 상세 조회",
            description = """
            특정 공고의 상세 정보를 조회합니다.
            - 조회 시 공고의 조회수가 1 증가합니다.
        """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "공고를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ApiResponse<AnnouncementDTO.Detail> getAnnouncementDetail(
            @Parameter(description = "공고 ID", example = "1", required = true)
            @PathVariable Long id)
    {
        announcementService.increaseViewCount(id);
        return ApiResponse.ok(announcementService.getAnnouncementDetail(id));
    }
}
