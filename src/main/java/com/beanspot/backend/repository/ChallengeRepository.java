package com.beanspot.backend.repository;

import com.beanspot.backend.entity.AnnouncementChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<AnnouncementChallenge, Long> {
    // 특정 모집 대상을 기준으로 조회하는 예시
    List<AnnouncementChallenge> findByParticipantTargetContaining(String target);
}