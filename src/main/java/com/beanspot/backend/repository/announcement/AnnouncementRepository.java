package com.beanspot.backend.repository.announcement;

import com.beanspot.backend.entity.announcement.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findAllByRecruitmentEnd(LocalDate recruitmentEnd);
}
