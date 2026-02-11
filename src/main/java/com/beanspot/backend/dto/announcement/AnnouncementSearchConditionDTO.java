package com.beanspot.backend.dto.announcement;

import com.beanspot.backend.entity.announcement.AnnouncementType;
import com.beanspot.backend.entity.search.SortType;
import lombok.Builder;
import lombok.Getter;

import java.time.YearMonth;
import java.util.List;

@Getter
@Builder
public class AnnouncementSearchConditionDTO {
    // keyword
    private String keyword;

    // filter
    private AnnouncementType type;
    private List<String> hashtags;
    private String region;
    private boolean onlyRecruiting;

    // 기간
    private YearMonth recruitmentMonth;
    private YearMonth activityMonth;

    // 지도 범위
    private Double minLat;
    private Double maxLat;
    private Double minLng;
    private Double maxLng;

    // 정렬
    private SortType sort;

    // paging
    private int page;
    private int size;
    private Integer limit;
}
