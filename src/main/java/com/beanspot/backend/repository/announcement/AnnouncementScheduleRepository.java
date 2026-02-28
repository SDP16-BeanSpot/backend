package com.beanspot.backend.repository.announcement;

import com.beanspot.backend.entity.announcement.AnnouncementSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementScheduleRepository extends JpaRepository<AnnouncementSchedule, Long> {
}
