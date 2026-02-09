package com.beanspot.backend.repository;

import com.beanspot.backend.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    // 특정 회원의 특정 날짜 투두 리스트 조회
    List<Todo> findAllByUserIdAndDate(Long memberId, LocalDate date);
}
