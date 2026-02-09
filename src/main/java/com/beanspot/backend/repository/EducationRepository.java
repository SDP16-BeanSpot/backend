package com.beanspot.backend.repository;

import com.beanspot.backend.entity.AnnouncementEducation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<AnnouncementEducation, Long> {
    // 참가비가 0원(무료)인 교육 공고만 조회하는 예시
    List<AnnouncementEducation> findByFee(Integer fee);
}