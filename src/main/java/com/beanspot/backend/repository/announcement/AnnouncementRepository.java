package com.beanspot.backend.repository.announcement;

import com.beanspot.backend.entity.announcement.Announcement;
import com.beanspot.backend.repository.es.AnnouncementSearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
}
