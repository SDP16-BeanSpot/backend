package com.beanspot.backend.repository;

import com.beanspot.backend.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional; // Optional을 사용해야 orElse(null)을 쓸 수 있습니다.

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    /**
     * 특정 사용자의 특정 기간(한 달) 동안의 일기 목록을 조회합니다.
     * 캘린더에 일기 아이콘을 표시할 때 사용합니다.
     */
    List<Diary> findAllByUserIdAndDateBetween(Long memberId, LocalDate startDate, LocalDate endDate);

    // [일별 상세 조회] 이번에 추가해야 할 메서드
    Optional<Diary> findByUserIdAndDate(Long memberId, LocalDate date);




}