package com.beanspot.backend.repository;

import com.beanspot.backend.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    // 모든 유형의 공고를 최신순으로 조회하는 예시
    List<Announcement> findAllByOrderByCreatedAtDesc();
}
