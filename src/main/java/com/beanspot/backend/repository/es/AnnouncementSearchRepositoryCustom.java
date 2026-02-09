package com.beanspot.backend.repository.es;

import com.beanspot.backend.entity.announcement.AnnouncementDocument;
import com.beanspot.backend.entity.announcement.AnnouncementType;
import com.beanspot.backend.entity.search.SortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;
import java.util.List;

public interface AnnouncementSearchRepositoryCustom {
    Page<AnnouncementDocument> search(
            String keyword,
            AnnouncementType type,
            String region,
            YearMonth recruitmentMonth,
            YearMonth activityMonth,
            Double minLat,
            Double maxLat,
            Double minLng,
            Double maxLng,
            boolean onlyRecruiting,
            SortType sort,
            Pageable pageable
    );

    List<String> autocomplete(String keyword, int limit);
}
