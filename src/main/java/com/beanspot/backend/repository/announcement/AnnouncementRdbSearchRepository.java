package com.beanspot.backend.repository.announcement;

import com.beanspot.backend.entity.announcement.Announcement;
import com.beanspot.backend.entity.announcement.AnnouncementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnnouncementRdbSearchRepository {
    Page<Announcement> search(
            String keyword,
            AnnouncementType type,
            String region,
            String sort,
            Pageable pageable
    );

    List<Announcement> findTop(
            String keyword,
            AnnouncementType type,
            String region,
            String sort,
            int limit
    );
}
