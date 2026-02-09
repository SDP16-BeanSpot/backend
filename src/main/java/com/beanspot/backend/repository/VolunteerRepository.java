package com.beanspot.backend.repository;

import com.beanspot.backend.entity.AnnouncementVolunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolunteerRepository extends JpaRepository<AnnouncementVolunteer, Long> {
    // 봉사시간 인증이 가능한 공고만 조회하는 예시 쿼리 메서드
    List<AnnouncementVolunteer> findByIsServiceHoursVerifiedTrue();
}
