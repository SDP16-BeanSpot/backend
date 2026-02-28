package com.beanspot.backend.controller;

import com.beanspot.backend.common.response.ApiResponse;
import com.beanspot.backend.security.CurrentUserId;
import com.beanspot.backend.service.search.SearchKeywordFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name="검색 UX 관련 API", description = "최근 검색어, 검색어 자동완성, 추천 검색어 등 검색 UX 개선을 위한 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchQueryController {


    private final SearchKeywordFacade searchKeywordFacade;

    @Operation(
            summary = "최근 검색어 조회",
            description = """
        로그인한 사용자의 최근 검색어 목록을 조회합니다.

        - 가장 최근에 검색한 키워드부터 반환됩니다.
        - 최대 개수는 서버 정책에 따라 제한될 수 있습니다.
        """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인이 필요함")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/recent")
    public ApiResponse<List<String>> recent(@Parameter(hidden = true) @CurrentUserId Long userId) {
        return ApiResponse.ok(searchKeywordFacade.getRecent(userId));
    }

    @Operation(
            summary = "최근 검색어 삭제",
            description = """
                        로그인한 사용자의 최근 검색어 중 특정 키워드를 삭제합니다.
                        """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인이 필요함"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값이 올바르지 않음")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/recent")
    public ApiResponse<?> deleteRecent(@Parameter(hidden = true) @CurrentUserId Long userId,
                                       @Parameter(description = "삭제할 검색 키워드",required = true) @RequestParam String keyword) {
        searchKeywordFacade.deleteRecent(userId, keyword);
        return ApiResponse.ok("키워드 삭제가 처리되었습니다.");
    }
    @Operation(
            summary = "최근 검색어 전체 삭제",
            description = "로그인한 사용자의 모든 최근 검색어를 삭제합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "로그인이 필요함")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/recent/all")
    public ApiResponse<?> deleteRecentAll(@Parameter(hidden = true) @CurrentUserId Long userId) {
        searchKeywordFacade.deleteAllRecent(userId);
        return ApiResponse.ok("최근 검색어가 모두 삭제되었습니다.");
    }
    @Operation(
            summary = "검색어 자동완성",
            description = """
            입력 중인 검색어(query)를 기반으로 자동완성 키워드 목록을 제공합니다.
            검색어의 앞부분(prefix) 기준으로 동작합니다.
        """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값이 올바르지 않음")
    })
    @GetMapping("/autocomplete")
    public ApiResponse<List<String>> autocomplete(@Parameter(description = "자동완성 기준 검색어", required = true)
                                                      @RequestParam String query) {
        return ApiResponse.ok(searchKeywordFacade.getAutoComplete(query));
    }
    @Operation(
            summary = "추천 검색어 조회",
            description = """
        사용자에게 노출할 추천 검색어 목록을 조회합니다.
        - 인기 검색어 기반으로 추천됩니다.
        - 기본적으로 상위 5개 키워드를 반환합니다.
        """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/recommend")
    public ApiResponse<List<String>> recommend() {
        return ApiResponse.ok(searchKeywordFacade.getRecommended(5));
    }
}
