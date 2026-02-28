package com.beanspot.backend.service.announcement;

import com.beanspot.backend.common.response.PageResponse;
import com.beanspot.backend.dto.announcement.AnnouncementSearchConditionDTO;
import com.beanspot.backend.dto.announcement.AnnouncementSummaryDTO;
import com.beanspot.backend.entity.announcement.AnnouncementDocument;
import com.beanspot.backend.entity.announcement.Announcement;
import com.beanspot.backend.entity.search.SortType;
import com.beanspot.backend.repository.announcement.AnnouncementRdbSearchRepository;
import com.beanspot.backend.repository.es.AnnouncementSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnnouncementSearchService {

    private final AnnouncementSearchRepository esRepository;
    private final AnnouncementRdbSearchRepository rdbRepository;

    // 공고 목록 검색 (사용자 / 홈 / 검색 페이지)
    public PageResponse<AnnouncementSummaryDTO> search(
            AnnouncementSearchConditionDTO condition
    ){
        log.info("[AnnouncementSearch] search start - condition={}", condition);
        // 홈 화면 (limit) (RDB)
        if (condition.getLimit() != null) {
            log.info("[AnnouncementSearch][RDB] limit search 시작 (limit={})", condition.getLimit());

            List<Announcement> list = rdbRepository.findTop(
                    condition.getKeyword(),
                    condition.getType(),
                    condition.getRegion(),
                    condition.getSort().name(),
                    condition.getLimit()
            );
            log.info("[AnnouncementSearch][RDB] limit search 완료 - resultSize={}", list.size());
            List<AnnouncementSummaryDTO> dtos = list.stream()
                    .map(AnnouncementSummaryDTO::from)
                    .toList();

            return PageResponse.of(dtos);
        }

        Pageable pageable = PageRequest.of(
                condition.getPage(),
                condition.getSize(),
                resolveSort(condition.getSort())
        );
        // ES 검색
        try{
            log.info("[AnnouncementSearch][ES] search 시도 - page={}, size={}",
                    condition.getPage(), condition.getSize());

            Page<AnnouncementDocument> esResult =
                    esRepository.search(
                            condition.getKeyword(),
                            condition.getType(),
                            condition.getRegion(),
                            condition.getActivityMethod(),
                            condition.getRecruitmentMonth(),
                            condition.getActivityMonth(),
                            condition.getMinLat(),
                            condition.getMaxLat(),
                            condition.getMinLng(),
                            condition.getMaxLng(),
                            condition.isOnlyRecruiting(),
                            condition.getSort(),
                            pageable
                    );
            log.info("[AnnouncementSearch][ES] search 성공 - totalElements={}, pageElements={}",
                    esResult.getTotalElements(),
                    esResult.getNumberOfElements());

            List<AnnouncementSummaryDTO> dtos = esResult.getContent()
                    .stream()
                    .map(AnnouncementSummaryDTO::from)
                    .toList();

            return PageResponse.of(esResult, dtos);
        }catch (Exception e) {
            // ES 장애 시 fallback
            log.error("[AnnouncementSearch][ES] search 실패 → RDB fallback", e);
            return searchFromRdbFallback(condition, pageable);
        }
    }

    private PageResponse<AnnouncementSummaryDTO> searchFromRdbFallback(
            AnnouncementSearchConditionDTO condition, Pageable pageable
    ){
        log.info("[AnnouncementSearch][RDB-FALLBACK] search");
        Page<Announcement> pageResult = rdbRepository.search(condition.getKeyword(),
                condition.getType(),
                condition.getRegion(),
                condition.getSort().name(),
                pageable
        );

        List<AnnouncementSummaryDTO> dtos = pageResult.getContent()
                .stream()
                .map(AnnouncementSummaryDTO::from)
                .toList();

        return PageResponse.of(pageResult, dtos);
    }

    private Sort resolveSort(SortType sortType) {

        return switch (sortType) {
            case POPULAR -> Sort.by(Sort.Direction.DESC, "viewCount");
            case LATEST -> Sort.by(Sort.Direction.DESC, "createdAt");
            default -> Sort.unsorted(); // Repository에서 직접 처리
        };
    }

}
